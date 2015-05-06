package com.an.wm.dao;

import com.an.core.model.BaseDao;
import com.an.wm.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class InventoryLogDao extends BaseDao<Item, Integer> {
    public InventoryLogDao() {
        super();
        namespace = "InventoryLogMapper";
    }


    public int save(Item stock) {
        return insert(stock);
    }

    public List<Item> selectByTimestamp(Item goods, Date invTime) {
        Map<String, Object> param = new HashMap<>();
        param.put("goodsCode", goods.getPn());
        param.put("timestamp", invTime);
        return sqlSession.selectList(namespace + ".selectByTimestamp", param);
    }
}
