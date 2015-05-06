package com.an.crm.dao;

import com.an.core.model.BaseDao;
import com.an.crm.entity.Coupon;
import org.springframework.stereotype.Repository;

import java.util.Random;

@Repository
public class CouponCodeDao extends BaseDao<Coupon, String> {
    public CouponCodeDao() {
        super();
        namespace = "CRM.CouponCodeMapper";
    }

    private static int no = 100;

    static String[] strArray = new String[]{"A", "B", "C", "D", "E", "F",
            "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S",
            "T", "U", "V", "W", "X", "Y", "Z"};
    Random ran = new Random();


    public int updateStatus(Coupon bill) {
        if (bill != null) {
            bill.setStatus("u");
            return sqlSession.update(namespace + ".updateByPrimaryKey", bill);
        } else
            return 0;
    }

    public String getNum(int len) {
        String salt = "";
        String Num = String.valueOf(no++);
        int i = 0;
        for (int bit = 0; bit < len; bit++) {
            salt += strArray[ran.nextInt(25)];
            if (ran.nextInt(i + 2) >= 1 && i < Num.length()) {
                salt += Num.charAt(i);
                i++;
            }
        }
        salt += Num.substring(i);
        no++;
        return salt;
    }
}