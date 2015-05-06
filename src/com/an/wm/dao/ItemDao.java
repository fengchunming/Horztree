package com.an.wm.dao;

import com.an.core.model.BaseDao;
import com.an.wm.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Repository
public class ItemDao extends BaseDao<Item, Integer> {

    public ItemDao() {
        super();
        namespace = "WM.ItemMapper";
    }

    public int save(Item goods) {
        if (goods.getItemId() > 0
                || selectByCode(goods.getPn()) != null) {
            return update(goods);
        } else {
            return insert(goods);
        }
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectKV");
    }

    public Item selectByCode(String pn) {
        return sqlSession.selectOne(namespace + ".selectByPn", pn);
    }

    public List<Item> selectByGoods(String goods) {
        return sqlSession.selectList(namespace + ".selectByGoodsId", goods);
    }

    public int updateMovingAveragePrice(Item goods) {
        return sqlSession
                .update(namespace + ".updateMovingAveragePrice", goods);
    }


}
