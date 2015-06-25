package com.an.trade.dao;

import com.an.core.model.BaseDao;
import com.an.trade.entity.Trade;
import com.an.trade.entity.TradeReport;
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
		// System.out.println(trade.getType());
		return super.insert(trade);
	}

	public int countByGroupId(int id) {
		return sqlSession.selectOne(namespace + ".countByGroupId", id);
	}

	public String nextCode() {
		return Util.CurrentTime("yyMMdd") + String.format("%04d", (int) sqlSession.selectOne("PublicMapper.nextVal", "wm_bill_code_seq"));
	}

	public int updateState(Trade bill) {
		return sqlSession.update(namespace + ".updateState", bill);
	}

	public List<TradeReport> report(Map<String, Object> params) {
		List<TradeReport> list = sqlSession.selectList(namespace + ".selectReport", params);
		for (int i = 0; i < list.size(); i++) {
			list.get(i).setId(i + 1);
		}
		return list;
	}
}
