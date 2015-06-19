package com.an.mm.dao;

import com.an.base.dao.RegionDao;
import com.an.base.entity.Region;
import com.an.core.exception.BadRequestException;
import com.an.core.model.BaseDao;
import com.an.mm.entity.Goods;
import com.an.utils.Util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GoodsDao extends BaseDao<Goods, Integer> {
	
    @Autowired
    private RegionDao regionDao;

	public GoodsDao() {
		super();
		namespace = "MM.GoodsMapper";
	}

	public int save(Goods goods) {
		if (goods.getId() != null && goods.getId() > 0) {
			return update(goods);
		} else {
			goods.setCode("G" + nextCode());
			return insert(goods);
		}
	}

	public String nextCode() {
		return Util.CurrentTime("yyMMdd") + String.format("%04d", (int) sqlSession.selectOne("PublicMapper.nextVal", "mm_goods_code_seq"));
	}

	public List<Goods> selectInventory(Map<String, Object> params) {
		return sqlSession.selectList(namespace + ".selectInventory", params);
	}

	public int countInventory(Map<String, Object> params) {
		return sqlSession.selectOne(namespace + ".countInventory", params);
	}

	public Goods selectGoodsInventory(Integer goodsId, Integer regionId) {
    	Map<String, Object> mParam = new HashMap<String, Object>();
    	mParam.put("goodsId", goodsId);
    	mParam.put("regionId", regionId);
		return sqlSession.selectOne(namespace + ".selectInventory", mParam);
	}
	
	/**
	 * 查出goods表中在集合code里面的id列表
	 * @param list
	 * @return
	 */
	public List<Goods> selectGoodsIdList(List<Goods> list) {
		return sqlSession.selectList(namespace + ".selectGoodsIdList", list);
	}
	
	/**
	 * 查出region表中在集合code里面的id列表
	 * @param list
	 * @return
	 */
	public List<Goods> selectRegionIdList(List<Goods> list) {
		return sqlSession.selectList(namespace + ".selectRegionIdList", list);
	}
	/**
	 * 批量导入库存数据，存在更新，不存在插入
	 * @param goods
	 * @return
	 */
	public int updateGoodsInventory(Goods goods) {
		return sqlSession.update(namespace + ".insertOrUpdateInventory", goods);
	}
	
	public int batchUpdateGoodsInventory(List<Goods> list) {
		return sqlSession.update(namespace + ".batchInsertOrUpdateInventory", list);
	}

	public int reduceStock(int goodsId, int regionId, int amount, boolean changeSoldVolume) {
		if (amount == 0) return 0;
    	Goods goods = selectGoodsInventory(goodsId, regionId);
		if (goods == null) {
			String regionName = regionDao.selectOne(regionId).getFullName();
			String goodsName = this.selectOne(goodsId).getName();
			throw new BadRequestException("仓库【" + regionName + "】中的商品【" + goodsName + "】不存在，无法完成库存扣减");
		} else {
			if (goods.getStockSum() - amount < 0) {
				String regionName = regionDao.selectOne(regionId).getFullName();
				String goodsName = this.selectOne(goodsId).getName();
				throw new BadRequestException("仓库【" + regionName + "】中的商品【" + goodsName + "】库存余量不足，无法完成扣减");
			}
    		goods.setStockSum(goods.getStockSum() - amount);
		}
		if (changeSoldVolume) {
			goods.setSoldVolume(goods.getSoldVolume() + amount);
		}
		return this.updateGoodsInventory(goods);
	}

	public int addStock(int goodsId, int regionId, int amount) {
		if (amount == 0) return 0;
    	Goods goods = selectGoodsInventory(goodsId, regionId);
    	if (goods == null) {
    		goods = new Goods();
    		goods.setId(goodsId);
    		goods.setRegionId(regionId);
    		goods.setStockSum(amount);
    		goods.setStockLocked(0);
    		goods.setStatus("1");
    	} else {
    		goods.setStockSum((goods.getStockSum() == null ? 0 : goods.getStockSum()) + amount);
    	}
    	return this.updateGoodsInventory(goods);
	}

}
