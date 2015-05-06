package com.an.core.mybatis.plugins;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.Properties;

/**
 * 数据库分页拦截器
 * <p/>
 * Created by karas on 5/29/14.
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class PaginationInterceptor implements Interceptor {
    // 日志对象
    protected static Logger logger = Logger.getLogger(PaginationInterceptor.class);
    private PageHelper helper;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];

        if (ms.getId().contains(".select") && parameterObject instanceof Map) {
            Map param = (Map) parameterObject;
            if (param.containsKey("_ST") && param.containsKey("_LM")) {
                Integer offset = Integer.parseInt(param.get("_ST").toString());
                Integer limit = Integer.parseInt(param.get("_LM").toString());

                BoundSql boundSql = ms.getBoundSql(parameterObject);
                //将参数中的MappedStatement替换为新的qs
                args[0] = helper.getPageMappedStatement(ms, boundSql);
                //动态sql时，boundSql在这儿通过新的ms获取
                if (boundSql == null) {
                    boundSql = ((MappedStatement) args[0]).getBoundSql(parameterObject);
                }
                //判断parameterObject，然后赋值
                args[1] = helper.setPageParameter(parameterObject, boundSql, offset, limit);
            }

        }
        return invocation.proceed();
    }


    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties p) {
        String dialect = p.getProperty("dialect");

        helper = new PageHelper(dialect);
    }

}
