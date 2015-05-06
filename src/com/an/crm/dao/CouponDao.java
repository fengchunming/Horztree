package com.an.crm.dao;

import com.an.core.model.BaseDao;
import com.an.crm.entity.Coupon;
import org.springframework.stereotype.Repository;

@Repository
public class CouponDao extends BaseDao<Coupon, Integer> {
    public CouponDao() {
        super();
        namespace = "CRM.CouponMapper";
    }


}

