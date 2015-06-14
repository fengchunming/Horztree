package com.an.mm.dao;

import com.an.core.model.BaseDao;
import com.an.mm.entity.Goods;
import com.an.wm.entity.Item;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GoodsCombDao extends BaseDao<Goods, Integer> {

	public GoodsCombDao() {
		super();
		namespace = "MM.GoodsCombMapper";
	}

	public int insertItem(Item item) {
		return sqlSession.insert(namespace + ".insertItem", item);
	}

	public List<Item> selectItemsByGoodsId(int goodsId) {
		return sqlSession.selectList(namespace + ".selectItemsByGoodsId", goodsId);
	}

}
