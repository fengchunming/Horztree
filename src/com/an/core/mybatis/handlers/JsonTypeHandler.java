package com.an.core.mybatis.handlers;

/**
 *javaType: List Simple DataType   <->   jdbcType: VARCHAR
 *
 * @author Karas
 * @date 2012-4-24
 *
 */

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.TypeHandler;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedJdbcTypes(JdbcType.VARCHAR)
public class JsonTypeHandler implements TypeHandler<Object> {
    private ObjectMapper jsonObjectMapper = new ObjectMapper();

    @Override
    public void setParameter(PreparedStatement arg0, int arg1,
                             Object arg2, JdbcType arg3) throws SQLException {
        String str = null;
        try {
            if (arg2 != null) {
                str = arg2.getClass().getName() + ":" + jsonObjectMapper.writeValueAsString(arg2);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        arg0.setString(arg1, str);

    }

    @Override
    public Object getResult(ResultSet arg0, int arg1) throws SQLException {
        return toClass(arg0.getString(arg1));
    }

    @Override
    public Object getResult(ResultSet arg0, String arg1)
            throws SQLException {
        return toClass(arg0.getString(arg1));
    }

    @Override
    public Object getResult(CallableStatement arg0, int arg1)
            throws SQLException {
        return toClass(arg0.getString(arg1));
    }

    private Object toClass(String data) {
        if (data == null) return null;
        String clas = data.substring(0, data.indexOf(":"));
        String json = data.substring(data.indexOf(":") + 1);
        Object list = null;
        try {
            list = jsonObjectMapper.readValue(json, Class.forName(clas));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }
}
