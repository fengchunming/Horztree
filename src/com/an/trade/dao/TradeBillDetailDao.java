package com.an.trade.dao;

import com.an.core.model.BaseDao;
import com.an.trade.entity.TradeBillDetail;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TradeBillDetailDao extends BaseDao<TradeBillDetail, Integer> {

    public TradeBillDetailDao() {
        super.namespace = "TradeBillDetailMapper";
    }

    public int save(TradeBillDetail line) {
        if (line.getId() != null && line.getId() != 0) {
            return update(line);
        } else {
            return insert(line);
        }
    }

    public List<TradeBillDetail> selectByBill(int billId) {
        return sqlSession.selectList(super.namespace + ".selectByBill", billId);
    }

    public int countByBill(int billId) {
        return sqlSession.selectOne(super.namespace + ".countByBill", billId);
    }

    public int deleteByBill(int id) {
        return sqlSession.delete(super.namespace + ".deleteByBill", id);
    }
}
