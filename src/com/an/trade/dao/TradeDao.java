package com.an.trade.dao;

import com.an.core.model.BaseDao;
import com.an.trade.entity.Trade;
import com.an.utils.Util;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class TradeDao extends BaseDao<Trade, Integer> {

    public TradeDao() {
        super();
        namespace = "TradeMapper";
    }

    public int insert(Trade trade) {
        trade.setBillCode("OF" + nextCode());
        System.out.println(trade.getType());
        return super.insert(trade);
    }

    public String nextCode() {
        return Util.CurrentTime("yyMMdd")
                + String.format("%04d", (int) sqlSession.selectOne(
                "PublicMapper.nextVal", "wm_bill_code_seq"));
    }

    public Trade selectByCode(String billCode) {
        return sqlSession.selectOne(namespace + ".selectByPrimaryKey", billCode);
    }

    public int updateState(Trade bill) {
        return sqlSession.update(namespace + ".updateState", bill);
    }

    public int updateRealQuantity(String billCode) {
        return sqlSession.update(namespace + ".updateRealQuantity", billCode);
    }

    public List report(Map<String, Object> params) {
        return sqlSession.selectList(namespace + ".selectReport", params);
    }
}
