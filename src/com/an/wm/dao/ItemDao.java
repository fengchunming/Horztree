package com.an.wm.dao;

import com.an.core.model.BaseDao;
import com.an.wm.entity.Item;

import org.springframework.stereotype.Repository;

@Repository
public class ItemDao extends BaseDao<Item, Integer> {

    public ItemDao() {
        super();
        namespace = "WM.ItemMapper";
    }

    public int save(Item goods) {
        if (goods.getId() != null && goods.getId() > 0) {
            return update(goods);
        } else {
            return insert(goods);
        }
    }
    
}
