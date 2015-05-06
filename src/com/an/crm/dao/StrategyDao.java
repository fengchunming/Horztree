package com.an.crm.dao;

import com.an.core.model.BaseDao;
import com.an.crm.entity.Strategy;
import org.springframework.stereotype.Repository;

@Repository
public class StrategyDao extends BaseDao<Strategy, Integer> {
    public StrategyDao() {
        super();
        namespace = "CRM.StrategyMapper";
    }

    public void saveGroups(Strategy goods) {
        sqlSession.delete(namespace + ".removeGroup", goods);
        if (goods.getGroups().size() > 0)
            sqlSession.insert(namespace + ".insertGroup", goods);
    }
}

