package com.an.core.mybatis.handlers;

/**
 *javaType: String   <->   jdbcType: TIME
 *
 * @author Karas
 * @date 2012-4-24
 *
 */

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

@MappedJdbcTypes(JdbcType.VARCHAR)
public class TimeTypeHandler implements TypeHandler<String> {
    private SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

    @Override
    public String getResult(ResultSet arg0, String arg1) throws SQLException {
        java.sql.Time sqlTime = arg0.getTime(arg1);
        if (sqlTime != null) {
            return sdf.format(new Date(sqlTime.getTime()));
        }
        return null;
    }

    @Override
    public String getResult(ResultSet arg0, int arg1) throws SQLException {
        java.sql.Time sqlTime = arg0.getTime(arg1);
        if (sqlTime != null) {
            return sdf.format(new Date(sqlTime.getTime()));
        }
        return null;
    }

    @Override
    public String getResult(CallableStatement arg0, int arg1)
            throws SQLException {
        java.sql.Time sqlTime = arg0.getTime(arg1);
        if (sqlTime != null) {
            return sdf.format(new Date(sqlTime.getTime()));
        }
        return null;
    }

    @Override
    public void setParameter(PreparedStatement arg0, int arg1, String arg2,
                             JdbcType arg3) throws SQLException {
        if (arg2 != null && !arg2.isEmpty())
            arg0.setTime(arg1, java.sql.Time.valueOf(arg2 + ":00"));
        else
            arg0.setTime(arg1, null);
    }
}