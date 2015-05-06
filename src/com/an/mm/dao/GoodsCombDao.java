package com.an.mm.dao;

import com.an.core.model.BaseDao;
import com.an.mm.entity.Goods;
import com.an.wm.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class GoodsCombDao extends BaseDao<Goods, Integer> {

    public GoodsCombDao() {
        super();
        namespace = "MM.GoodsCombMapper";
    }

    public int insertItem(Item item) {
       return sqlSession.insert(namespace + ".insertItem", item);
    }

    public List<Item> selectItems(Map<String, Object> mParam) {
        return sqlSession.selectList(namespace + ".selectItems", mParam);
    }

    public List<Item> selectDirectItems(Map<String, Object> mParam) {
        return sqlSession.selectList(namespace + ".selectDirectItems", mParam);
    }
}
