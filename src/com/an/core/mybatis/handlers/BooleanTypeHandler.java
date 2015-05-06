/**
 *
 */
package com.an.core.mybatis.handlers;

/**
 *javaType: JSONObject   <->   jdbcType: VARCHAR
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

@MappedJdbcTypes(JdbcType.VARCHAR)
public class BooleanTypeHandler implements TypeHandler<Boolean> {

    @Override
    public Boolean getResult(ResultSet arg0, String arg1) throws SQLException {
        String sqlChar = arg0.getString(arg1);
        return "t".equals(sqlChar);
    }

    @Override
    public Boolean getResult(ResultSet arg0, int arg1) throws SQLException {
        String sqlChar = arg0.getString(arg1);
        return "t".equals(sqlChar);
    }

    @Override
    public Boolean getResult(CallableStatement arg0, int arg1)
            throws SQLException {
        String sqlChar = arg0.getString(arg1);
        return "t".equals(sqlChar);
    }

    @Override
    public void setParameter(PreparedStatement arg0, int arg1, Boolean arg2,
                             JdbcType arg3) throws SQLException {
        if (arg2 != null && arg2)
            arg0.setString(arg1, "t");
        else
            arg0.setString(arg1, "f");
    }

}