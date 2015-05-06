package com.an.wm.dao;

import com.an.core.model.BaseDao;
import com.an.utils.Util;
import com.an.wm.entity.WorkBill;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WorkBillDao extends BaseDao<WorkBill, Integer> {
    public WorkBillDao() {
        super();
        namespace = "WmBillMapper";
    }

    public int updateStatus(WorkBill bill) {
        return sqlSession.update(namespace + ".updateStatus", bill);
    }

    public String nextCode() {
        return Util.CurrentTime("yyMMdd")
                + String.format("%04d", (int) sqlSession.selectOne(
                "PublicMapper.nextVal", "wm_bill_code_seq"));
    }

    public int save(WorkBill bill) {
        if (bill.getId() > 0) {
            return update(bill);
        } else {
            bill.setBillCode(bill.getType() + nextCode());
            return insert(bill);
        }
    }

    public WorkBill selectByCode(String billCode) {
        return sqlSession.selectOne(namespace + ".selectByCode",billCode);
    }

    public WorkBill selectOrInit(String billCode, String billType) {
        if (billCode == null || billCode.equals("0")) {
            WorkBill bill = new WorkBill();
            bill.setBillCode(billType + nextCode());
            bill.setType(billType);
            bill.setBillDate(new Date());
            return bill;
        } else {

            WorkBill bill = selectByCode(billCode);
            if (bill == null) {
                bill = new WorkBill();
                bill.setBillCode(billCode);
                bill.setType(billType);
                bill.setBillDate(new Date());
            }
            return bill;
        }
    }

    public List<WorkBill> selectByOrigin(String billCode, String status) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("originBill", billCode);
        params.put("status", status);
        return sqlSession.selectList(namespace + ".selectByExample", params);
    }

    public WorkBill selectByExample(Map<String, Object> params) {
        return sqlSession.selectOne(namespace + ".selectByExample", params);
    }

}
