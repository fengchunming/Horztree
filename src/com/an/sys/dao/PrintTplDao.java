package com.an.sys.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.PrintTpl;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PrintTplDao extends BaseDao<PrintTpl, Integer> {

    public PrintTplDao() {
        super();
        namespace = "PrintTplMapper";
    }

    public PrintTpl selectByType(String type) {
        Map<String, Object> param = new HashMap<>();
        param.put("billType", type);
        return sqlSession.selectOne(namespace + ".selectByExample", param);
    }

}
