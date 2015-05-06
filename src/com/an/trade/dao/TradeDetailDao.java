package com.an.trade.dao;

import com.an.core.model.BaseDao;
import com.an.wm.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TradeDetailDao extends BaseDao<Item, Integer> {

    public TradeDetailDao() {
        super.namespace = "TradeDetailMapper";
    }


    public int save(Item line) {
        if (line.getId() != null && line.getId() != 0) {
            return update(line);
        } else {
            return insert(line);
        }
    }

    public int sync(Item line) {
        if (selectByOrigin(line.getOriginNo()) != null) {
            return updateByOrigin(line);
        } else {
            return insert(line);
        }
    }

    public List<Item> selectByBill(int code) {
        return sqlSession.selectList(super.namespace + ".selectByBill", code);
    }

    public int countByBill(int code) {
        return sqlSession.selectOne(super.namespace + ".countByBill", code);
    }

    public Item selectByOrigin(String originNo) {
        return sqlSession.selectOne(super.namespace + ".selectByOrigin",
                originNo);
    }

    public int updateByOrigin(Item item) {
        return sqlSession.update(super.namespace + ".updateByOrigin", item);
    }

//    public int updateBillCode(String originBillCode, String billCode) {
//        Map<String, ?> params = new HashMap<String, String>();
//        return sqlSession.update(super.namespace + ".updateBillCode", params);
//    }

    public int deleteByBill(int id) {
        return sqlSession.delete(super.namespace + ".deleteByBill", id);

    }
}
