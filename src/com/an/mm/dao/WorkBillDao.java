package com.an.mm.dao;

import com.an.core.model.BaseDao;
import com.an.mm.entity.WorkBill;
import com.an.utils.Util;

import org.springframework.stereotype.Repository;

@Repository
public class WorkBillDao extends BaseDao<WorkBill, Integer> {
	public WorkBillDao() {
		super();
		namespace = "WorkBillMapper";
	}

	public int updateDealStatus(WorkBill bill) {
		return sqlSession.update(namespace + ".updateDealStatus", bill);
	}

	public String nextCode() {
		return Util.CurrentTime("yyMMdd") + String.format("%04d", (int) sqlSession.selectOne("PublicMapper.nextVal", "wm_bill_code_seq"));
	}

	public int save(WorkBill bill) {
		if (bill.getId() != null && bill.getId() > 0) {
			return update(bill);
		} else {
			bill.setBillCode(bill.getBillType() + nextCode());
			return insert(bill);
		}
	}

}
