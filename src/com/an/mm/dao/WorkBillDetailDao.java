package com.an.mm.dao;

import com.an.core.model.BaseDao;
import com.an.mm.entity.WorkBillDetail;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WorkBillDetailDao extends BaseDao<WorkBillDetail, Integer> {
	public WorkBillDetailDao() {
		super();
		namespace = "WorkBillDetailMapper";
	}

	public int save(WorkBillDetail detail) {
		if (detail.getId() != null && detail.getId() > 0) {
			return update(detail);
		} else {
			return insert(detail);
		}
	}

	public List<WorkBillDetail> selectByBill(int billId) {
		Map<String, Object> param = new HashMap<>();
		param.put("billId", billId);
		return this.selectList(param);
	}

	public int countByBill(int billId) {
		return sqlSession.selectOne(namespace + ".countByBill", billId);
	}

	public int deleteByBill(int billId) {
		return sqlSession.delete(namespace + ".deleteByBill", billId);
	}

}
