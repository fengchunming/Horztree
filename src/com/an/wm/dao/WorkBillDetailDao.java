package com.an.wm.dao;

import com.an.core.model.BaseDao;
import com.an.wm.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class WorkBillDetailDao extends BaseDao<Item, Integer> {
    public WorkBillDetailDao() {
        super();
        namespace = "WmBillDetailMapper";
    }

    public int insert(Item item) {
        item.setStatus("0");
        if (item.getBatchCode() == null || item.getBatchCode().isEmpty())
            item.setBatchCode(String.format("%06d", (int) sqlSession.selectOne("PublicMapper.nextVal", "stock_lot_code_seq")));
        return super.insert(item);
    }

    public int save(Item item) {
        if (item.getId() != null && item.getId() > 0) {
            return update(item);
        } else {
            return insert(item);
        }
    }

    public int countByBill(int id) {
        return sqlSession.selectOne(namespace + ".countByBill", id);
    }

    public Collection<Item> selectByBill(int id) {
        Map<String, Object> param = new HashMap<>();
        param.put("billId", id);
        return this.selectList(param);
    }

    public int deleteByBill(int id) {
        return sqlSession.delete(namespace + ".deleteByBill", id);
    }


    public Collection<Item> selectByOriginCode(String billCode) {
        Map<String, Object> param = new HashMap<>();
        param.put("originBill", billCode);
        return this.selectList(param);
    }

    public int updateRealQuantity(String billCode) {
        return sqlSession.update(namespace + ".updateRealQuantity", billCode);
    }

    public int deleteByOriginCode(String billCode) {
        return sqlSession.delete(namespace + ".deleteByOriginCode", billCode);
    }
}
