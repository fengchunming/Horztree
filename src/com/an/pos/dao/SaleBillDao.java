package com.an.pos.dao;

import com.an.core.model.BaseDao;
import com.an.pos.entity.SaleBill;
import org.springframework.stereotype.Repository;

@Repository
public class SaleBillDao extends BaseDao<SaleBill, String> {

    public SaleBillDao() {
        super();
        namespace = "CategoryMapper";
    }

    public int save(SaleBill line) {
        if (selectOne(line.getBillCode()) != null) {
            return update(line);
        } else {
            return insert(line);
        }
    }
}
