package com.an.pos.dao;

import com.an.core.model.BaseDao;
import com.an.pos.entity.Cashier;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class CashierDao extends BaseDao<Cashier, String> {

    public CashierDao() {
        super();
        namespace = "CashierMapper";
    }


    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectCashierKV");
    }

}
