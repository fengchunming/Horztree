package com.an.core.mybatis.handlers;

/**
 *javaType: String   <->   jdbcType: TIMESTAMP
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

@MappedJdbcTypes(JdbcType.TIMESTAMP)
public class TimestampTypeHandler implements TypeHandler<String> {

    @Override
    public String getResult(ResultSet arg0, String arg1) throws SQLException {
        java.sql.Timestamp sqlTimestamp = arg0.getTimestamp(arg1);
        if (sqlTimestamp != null) {
            return Util.StampFmt.format(new Date(sqlTimestamp.getTime()));
        }
        return null;
    }

    @Override
    public String getResult(ResultSet arg0, int arg1) throws SQLException {
        java.sql.Timestamp sqlTimestamp = arg0.getTimestamp(arg1);
        if (sqlTimestamp != null) {
            return Util.StampFmt.format(new Date(sqlTimestamp.getTime()));
        }
        return null;
    }

    @Override
    public String getResult(CallableStatement arg0, int arg1)
            throws SQLException {
        java.sql.Timestamp sqlTimestamp = arg0.getTimestamp(arg1);
        if (sqlTimestamp != null) {
            return Util.StampFmt.format(new Date(sqlTimestamp.getTime()));
        }
        return null;
    }

    @Override
    public void setParameter(PreparedStatement arg0, int arg1, String arg2,
                             JdbcType arg3) throws SQLException {
        if (arg2 == null)
            arg0.setTimestamp(arg1, null);
        else {
            if (arg2.matches("(\\d{4}-\\d{2}-\\d{2}) (\\d{2}:\\d{2})"))
                arg2 += ":00";
            arg0.setTimestamp(arg1, java.sql.Timestamp.valueOf(arg2));
        }
    }

}