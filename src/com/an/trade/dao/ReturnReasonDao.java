package com.an.trade.dao;

import com.an.core.model.BaseDao;
import com.an.trade.entity.ReturnReason;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class ReturnReasonDao extends BaseDao<ReturnReason, Integer> {

    public ReturnReasonDao() {
        super();
        namespace = "Trade.ReturnReasonMapper";
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectReturnReasonKV");
    }
}
