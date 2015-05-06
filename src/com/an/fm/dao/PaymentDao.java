package com.an.fm.dao;

import com.an.core.model.BaseDao;
import com.an.fm.entity.Payment;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class PaymentDao extends BaseDao<Payment, Integer> {

    public PaymentDao() {
        super();
        namespace = "FM.PaymentMapper";
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectKV");
    }

}
