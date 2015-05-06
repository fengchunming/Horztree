package com.an.core.mybatis.plugins;

import org.apache.ibatis.builder.SqlSourceBuilder;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapperFactory;
import org.apache.ibatis.scripting.xmltags.DynamicContext;
import org.apache.ibatis.scripting.xmltags.DynamicSqlSource;
import org.apache.ibatis.scripting.xmltags.MixedSqlNode;
import org.apache.ibatis.scripting.xmltags.SqlNode;
import org.apache.ibatis.session.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mybatis - sql工具，获取分页和count的MappedStatement，设置分页参数
 */
public class PageHelper {
    //分页的id后缀
    private static final String SUFFIX_PAGE = "_PageHelper";
    //第一个分页参数
    private static final String PAGEPARAMETER_FIRST = "First" + SUFFIX_PAGE;
    //第二个分页参数
    private static final String PAGEPARAMETER_SECOND = "Second" + SUFFIX_PAGE;

    private static final ObjectFactory DEFAULT_OBJECT_FACTORY = new DefaultObjectFactory();
    private static final ObjectWrapperFactory DEFAULT_OBJECT_WRAPPER_FACTORY = new DefaultObjectWrapperFactory();

    /**
     * 反射对象，增加对低版本Mybatis的支持
     *
     * @param object 反射对象
     * @return
     */
    private static MetaObject forObject(Object object) {
        return MetaObject.forObject(object, DEFAULT_OBJECT_FACTORY, DEFAULT_OBJECT_WRAPPER_FACTORY);
    }

    private Parser sqlParser;

    //数据库方言 - 使用枚举限制数据库类型
    public enum Dialect {
        mysql, oracle, hsqldb, postgresql
    }

    /**
     * 构造方法
     *
     * @param strDialect
     */
    public PageHelper(String strDialect) {
        if (strDialect == null || "".equals(strDialect)) {
            throw new IllegalArgumentException("Mybatis分页插件无法获取dialect参数!");
        }
        try {
            Dialect dialect = Dialect.valueOf(strDialect);
            sqlParser = SimpleParser.newParser(dialect);

        } catch (IllegalArgumentException e) {
            String dialects = null;
            for (Dialect d : Dialect.values()) {
                if (dialects == null) {
                    dialects = d.toString();
                } else {
                    dialects += "," + d;
                }
            }
            throw new IllegalArgumentException("Mybatis分页插件dialect参数值错误，可选值为[" + dialects + "]");
        }
    }

    /**
     * 设置分页参数
     *
     * @param parameterObject
     * @param boundSql
     * @param offset
     * @param limit
     * @return
     */
    public Map setPageParameter(Object parameterObject, BoundSql boundSql, int offset, int limit) {
        return sqlParser.setPageParameter(parameterObject, boundSql, offset, limit);
    }


    /**
     * 获取分页查询的MappedStatement
     *
     * @param ms
     * @param boundSql
     * @return
     */
    public MappedStatement getPageMappedStatement(MappedStatement ms, BoundSql boundSql) {
        return getMappedStatement(ms, boundSql, SUFFIX_PAGE);
    }

    /**
     * 处理SQL
     */
    public static interface Parser {
        String getPageSql(String sql);

        Map setPageParameter(Object parameterObject, BoundSql boundSql, int offset, int limit);
    }

    public static abstract class SimpleParser implements Parser {
        public static Parser newParser(Dialect dialect) {
            Parser parser;
            switch (dialect) {
                case mysql:
                    parser = new MysqlParser();
                    break;
                case oracle:
                    parser = new OracleParser();
                    break;
                case hsqldb:
                    parser = new HsqldbParser();
                    break;
                case postgresql:
                default:
                    parser = new PostgreSQLParser();
            }
            return parser;
        }


        /**
         * 获取分页sql - 如果要支持其他数据库，修改这里就可以
         *
         * @param sql 原查询sql
         * @return 返回分页sql
         */
        public abstract String getPageSql(String sql);

        public Map setPageParameter(Object parameterObject, BoundSql boundSql, int offset, int limit) {
            Map paramMap;
            if (parameterObject == null) {
                paramMap = new HashMap();
            } else if (parameterObject instanceof Map) {
                paramMap = (Map) parameterObject;
            } else {
                paramMap = new HashMap();
                //这里以及下面使用的地方，主要解决一个参数时的问题，例如使用一个参数Country使用id属性时，不这样处理会导致id=Country
                MetaObject metaObject = forObject(parameterObject);
                if (boundSql.getParameterMappings() != null && boundSql.getParameterMappings().size() > 0) {
                    for (ParameterMapping parameterMapping : boundSql.getParameterMappings()) {
                        String name = parameterMapping.getProperty();
                        if (!name.equals(PAGEPARAMETER_FIRST)
                                && !name.equals(PAGEPARAMETER_SECOND)) {
                            if (parameterMapping.getJavaType().isAssignableFrom(parameterObject.getClass())) {
                                paramMap.put(name, parameterObject);
                            } else {
                                paramMap.put(name, metaObject.getValue(name));
                            }
                        }
                    }
                }
            }
            return paramMap;
        }
    }

    //Mysql
    private static class MysqlParser extends SimpleParser {
        @Override
        public String getPageSql(String sql) {
            return sql + " limit ?,?";
        }

        @Override
        public Map setPageParameter(Object parameterObject, BoundSql boundSql, int offset, int limit) {
            Map paramMap = super.setPageParameter(parameterObject, boundSql, offset, limit);
            paramMap.put(PAGEPARAMETER_FIRST, offset);
            paramMap.put(PAGEPARAMETER_SECOND, limit);
            return paramMap;
        }
    }

    //Oracle
    private static class OracleParser extends SimpleParser {
        @Override
        public String getPageSql(String sql) {
            StringBuilder sqlBuilder = new StringBuilder(sql.length() + 120);
            sqlBuilder.append("select * from ( select tmp_page.*, rownum row_id from ( ");
            sqlBuilder.append(sql);
            sqlBuilder.append(" ) tmp_page where rownum <= ? ) where row_id > ?");
            return sqlBuilder.toString();
        }

        @Override
        public Map setPageParameter(Object parameterObject, BoundSql boundSql, int offset, int limit) {
            Map paramMap = super.setPageParameter(parameterObject, boundSql, offset, limit);
            paramMap.put(PAGEPARAMETER_FIRST, offset);
            paramMap.put(PAGEPARAMETER_SECOND, offset + limit);
            return paramMap;
        }
    }

    //Oracle
    private static class HsqldbParser extends SimpleParser {
        @Override
        public String getPageSql(String sql) {
            StringBuilder sqlBuilder = new StringBuilder(sql.length() + 20);
            sqlBuilder.append(sql);
            sqlBuilder.append(" limit ? offset ?");
            return sqlBuilder.toString();
        }

        @Override
        public Map setPageParameter(Object parameterObject, BoundSql boundSql, int offset, int limit) {
            Map paramMap = super.setPageParameter(parameterObject, boundSql, offset, limit);
            paramMap.put(PAGEPARAMETER_FIRST, limit);
            paramMap.put(PAGEPARAMETER_SECOND, offset);
            return paramMap;
        }
    }

    //PostgreSQL
    private static class PostgreSQLParser extends SimpleParser {
        @Override
        public String getPageSql(String sql) {
            StringBuilder sqlBuilder = new StringBuilder(sql.length() + 50);
            sqlBuilder.append("select * from (");
            sqlBuilder.append(sql);
            sqlBuilder.append(") as tmp_page limit ? offset ?");
            return sqlBuilder.toString();
        }

        @Override
        public Map setPageParameter(Object parameterObject, BoundSql boundSql, int offset, int limit) {
            Map paramMap = super.setPageParameter(parameterObject, boundSql, offset, limit);
            paramMap.put(PAGEPARAMETER_FIRST, limit);
            paramMap.put(PAGEPARAMETER_SECOND, offset);
            return paramMap;
        }
    }

    /**
     * 自定义简单SqlSource
     */
    private class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }

        public BoundSql getBoundSql() {
            return boundSql;
        }
    }

    /**
     * 自定义动态SqlSource
     */
    private class MyDynamicSqlSource implements SqlSource {
        private Configuration configuration;
        private SqlNode rootSqlNode;

        public MyDynamicSqlSource(Configuration configuration, SqlNode rootSqlNode) {
            this.configuration = configuration;
            this.rootSqlNode = rootSqlNode;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            DynamicContext context = new DynamicContext(configuration, parameterObject);
            rootSqlNode.apply(context);
            SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
            Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
            SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
            BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
            //设置条件参数
            for (Map.Entry<String, Object> entry : context.getBindings().entrySet()) {
                boundSql.setAdditionalParameter(entry.getKey(), entry.getValue());
            }
            BoundSqlSqlSource boundSqlSqlSource = new BoundSqlSqlSource(boundSql);
            boundSqlSqlSource = getPageSqlSource(configuration, boundSqlSqlSource);
            return boundSqlSqlSource.getBoundSql();
        }
    }


    /**
     * 获取ms - 在这里对新建的ms做了缓存，第一次新增，后面都会使用缓存值
     *
     * @param ms
     * @param boundSql
     * @param suffix
     * @return
     */
    private MappedStatement getMappedStatement(MappedStatement ms, BoundSql boundSql, String suffix) {
        MappedStatement qs = null;
        try {
            qs = ms.getConfiguration().getMappedStatement(ms.getId() + suffix);
        } catch (Exception e) {
            //ignore
        }
        if (qs == null) {
            //创建一个新的MappedStatement
            qs = newMappedStatement(ms, getNewSqlSource(ms, new BoundSqlSqlSource(boundSql)), suffix);
            try {
                ms.getConfiguration().addMappedStatement(qs);
            } catch (Exception e) {
                //ignore
            }
        }
        return qs;
    }

    /**
     * 新建count查询和分页查询的MappedStatement
     *
     * @param ms
     * @param newSqlSource
     * @param suffix
     * @return
     */
    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource newSqlSource, String suffix) {
        String id = ms.getId() + suffix;
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), id, newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());

        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }

        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    /**
     * 判断当前执行的是否为动态sql
     *
     * @param ms
     * @return
     */
    public boolean isDynamic(MappedStatement ms) {
        return ms.getSqlSource() instanceof DynamicSqlSource;
    }

    /**
     * 获取新的sqlSource
     *
     * @param ms
     * @param newSqlSource
     * @return
     */
    private SqlSource getNewSqlSource(MappedStatement ms, BoundSqlSqlSource newSqlSource) {
        //从XMLLanguageDriver.java和XMLScriptBuilder.java可以看出只有两种SqlSource
        //如果是动态sql
        if (isDynamic(ms)) {
            MetaObject msObject = forObject(ms);
            SqlNode sqlNode = (SqlNode) msObject.getValue("sqlSource.rootSqlNode");
            MixedSqlNode mixedSqlNode;
            if (sqlNode instanceof MixedSqlNode) {
                mixedSqlNode = (MixedSqlNode) sqlNode;
            } else {
                List<SqlNode> contents = new ArrayList<>(1);
                contents.add(sqlNode);
                mixedSqlNode = new MixedSqlNode(contents);
            }
            return new MyDynamicSqlSource(ms.getConfiguration(), mixedSqlNode);

        } else {
            //改为分页sql
            return getPageSqlSource(ms.getConfiguration(), newSqlSource);
        }
    }

    /**
     * 获取分页的sqlSource
     *
     * @param configuration
     * @param newSqlSource
     * @return
     */
    private BoundSqlSqlSource getPageSqlSource(Configuration configuration, BoundSqlSqlSource newSqlSource) {
        String sql = newSqlSource.getBoundSql().getSql();
        //改为分页sql
        MetaObject sqlObject = forObject(newSqlSource);
        sqlObject.setValue("boundSql.sql", sqlParser.getPageSql(sql));
        //添加参数映射
        List<ParameterMapping> newParameterMappings = new ArrayList<>();
        newParameterMappings.addAll(newSqlSource.getBoundSql().getParameterMappings());
        newParameterMappings.add(new ParameterMapping.Builder(configuration, PAGEPARAMETER_FIRST, Integer.class).build());
        newParameterMappings.add(new ParameterMapping.Builder(configuration, PAGEPARAMETER_SECOND, Integer.class).build());
        sqlObject.setValue("boundSql.parameterMappings", newParameterMappings);
        return newSqlSource;
    }


}
