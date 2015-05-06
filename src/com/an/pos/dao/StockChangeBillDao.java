package com.an.pos.dao;

import com.an.core.model.BaseDao;
import com.an.pos.entity.StockChangeBill;
import org.springframework.stereotype.Repository;

@Repository
public class StockChangeBillDao extends BaseDao<StockChangeBill, String> {

    public StockChangeBillDao() {
        super();
        namespace = "CategoryMapper";
    }


    public int save(StockChangeBill bill) {
        if (selectOne(bill.getBillCode()) != null) {
            return update(bill);
        } else {
            return insert(bill);
        }
    }
}
