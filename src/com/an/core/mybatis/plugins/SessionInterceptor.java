package com.an.core.mybatis.plugins;

import com.an.core.service.SecurityFilter;
import com.an.sys.entity.User;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Properties;

@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class})})
public class SessionInterceptor implements Interceptor {
    public Object intercept(Invocation invocation) throws Throwable {

        if (!(invocation.getTarget() instanceof RoutingStatementHandler))
            return invocation.proceed();

        RoutingStatementHandler statementHandler = (RoutingStatementHandler) invocation
                .getTarget();
        BoundSql boundSql = statementHandler.getBoundSql();

        User user = SecurityFilter.threadLocal.get();
        if (user != null) {
            String filterSql = generateSql(boundSql.getSql(), user);
            setFieldValue(boundSql, "sql", filterSql);
        }
        return invocation.proceed();
    }

    private String generateSql(String sql, User user) {
        sql = sql.replaceAll("#userId", String.valueOf(user.getUserId()));
        sql = sql.replaceAll("#staffCode", String.valueOf(user.getStaffCode()));


        StringBuilder groups = new StringBuilder();
        for (Object id : user.getGroups()) {
            if (groups.length() > 0) groups.append(',');
            groups.append(id);
        }
        sql = sql.replaceAll("#groups", groups.toString());

//        if (user.getMerchant() != null) {
//            sql = sql.replaceAll("#merchId",
//                    String.valueOf(user.getMerchant().getId()));
//            sql = sql.replaceAll("#merchCode",
//                    String.valueOf(user.getMerchant().getMerchCode()));
//        }else{
//            sql = sql.replaceAll("#merchId", "0");
//        }

        StringBuilder roles = new StringBuilder();
        for (Object id : user.getRoleId()) {
            if (roles.length() > 0) roles.append(',');
            roles.append(id);
        }
        sql = sql.replaceAll("#roles", roles.toString());

        return sql;
    }

    /**
     * 用反射设置对象的属性值
     */
    private void setFieldValue(Object obj, String fieldName, Object fieldValue)
            throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, fieldValue);
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties arg0) {

    }
}
