package com.an.core.mybatis.handlers;

/**
 *javaType: String   <->   jdbcType: DATE
 *
 * @author Karas
 * @date 2012-4-24
 *
 */

import com.an.utils.Util;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@MappedJdbcTypes(JdbcType.DATE)
public class DateTypeHandler implements TypeHandler<String> {

    @Override
    public String getResult(ResultSet arg0, String arg1) throws SQLException {
        java.sql.Date sqlDate = arg0.getDate(arg1);
        if (sqlDate != null) {
            return Util.DateFmt.format(new Date(sqlDate.getTime()));
        }
        return null;
    }

    @Override
    public String getResult(ResultSet arg0, int arg1) throws SQLException {
        java.sql.Date sqlDate = arg0.getDate(arg1);
        if (sqlDate != null) {
            return Util.DateFmt.format(new Date(sqlDate.getTime()));
        }
        return null;
    }

    @Override
    public String getResult(CallableStatement arg0, int arg1)
            throws SQLException {
        java.sql.Date sqlDate = arg0.getDate(arg1);
        if (sqlDate != null) {
            return Util.DateFmt.format(new Date(sqlDate.getTime()));
        }
        return null;
    }

    @Override
    public void setParameter(PreparedStatement arg0, int arg1, String arg2,
                             JdbcType arg3) throws SQLException {
        if (arg2 != null && !arg2.isEmpty())
            arg0.setDate(arg1, java.sql.Date.valueOf(arg2));
        else
            arg0.setDate(arg1, null);
    }

}