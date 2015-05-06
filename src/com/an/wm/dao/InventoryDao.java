package com.an.wm.dao;

import com.an.core.model.BaseDao;
import com.an.wm.entity.Item;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InventoryDao extends BaseDao<Item, Integer> {
    public InventoryDao() {
        super();
        namespace = "InventoryMapper";
    }

    public int save(Item stock) {
        if (stock.getStockId() != null && stock.getStockId() > 0) {
            if (stock.getRealQuantity().compareTo(new BigDecimal(0)) == 0)
                stock.setStatus("d");
            return update(stock);
        } else {
            return insert(stock);
        }
    }

    public int countByBill(String billCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("billCode", billCode);
        return count(params);
    }

    public Collection<Item> selectByBill(String billCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("billCode", billCode);
        return selectList(params);
    }


    public List<Map> summaryList(Map<String, Object> mParam) {
        return sqlSession.selectList(namespace + ".selectSummary", mParam);
    }

    public int summaryCount(Map<String, Object> mParam) {
        return sqlSession.selectOne(namespace + ".countSummary", mParam);
    }
}
