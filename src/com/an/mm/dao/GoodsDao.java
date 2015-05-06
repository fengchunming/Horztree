package com.an.mm.dao;

import com.an.core.model.BaseDao;
import com.an.mm.entity.Goods;
import com.an.wm.entity.Item;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class GoodsDao extends BaseDao<Goods, Integer> {

    public GoodsDao() {
        super();
        namespace = "MM.GoodsMapper";
    }

    public int save(Goods goods) {
        if (goods.getGoodsId() > 0
                || selectByCode(goods.getPn()) != null) {
            return update(goods);
        } else {
            return insert(goods);
        }
    }

    public void saveCategory(Goods goods) {
        sqlSession.delete(namespace + ".removeCategory", goods);
        if (goods.getCategory().size() > 0)
            sqlSession.insert(namespace + ".insertCategory", goods);
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectGoodsKV");
    }

    public Goods selectByCode(String goodsCode) {
        return sqlSession.selectOne(namespace + ".selectByCode", goodsCode);
    }

    public void updateStock(Integer groupId, String pn, Integer itemId) {
        // 查询相关商品 及 货品 （pn = goodsCode or  itemId = combo.item_id)
        Map<String, Object> param = new HashMap<>();
        param.put("groupId", groupId);
        param.put("itemId", itemId);
        param.put("goodsCode", pn);
        List<Goods> goodsList = sqlSession.selectList("MM.GoodsCombMapper.selectLinkGoods", param);
        // 查询相关货品库存 pn = pn  or item_id = combo.item_id
        // 计算当前可用商品库存
        /*
        for (Goods goods : goodsList) {
            param.put("goodsId", goods.getGoodsId());
            List<Item> itemList = sqlSession.selectList("MM.GoodsCombMapper.selectLinkItem", param);
            goods.setQuantity(itemList.get(0).getRealQuantity().divide(itemList.get(0).getAdjustQuantity(), BigDecimal.ROUND_FLOOR));
            for (Item item : itemList) {
                if (item.getRealQuantity().divide(item.getAdjustQuantity(), BigDecimal.ROUND_FLOOR).compareTo(goods.getQuantity()) < 0)
                    goods.setQuantity(item.getRealQuantity());
            }
            sqlSession.update(namespace + ".updateStock", goods);
            
        }*/
        
        //--临时只处理一对一关系--
        if (goodsList != null && goodsList.size() > 0) {
	        Goods goods = goodsList.get(0);
	        
	        param.put("warehouse", groupId);
	        param.put("pn", pn);
	        List<Map> items = sqlSession.selectList(namespace + ".selectStock", param);
	        if (items != null && items.size() > 0) {
	        	BigDecimal quantity = (BigDecimal)items.get(0).get("quantity");
		        Map stock = selectStocksBy2Gids(groupId, goods.getGoodsId());
		        updateStocksBy2Gids(groupId, goods.getGoodsId(), quantity.intValue(), (Integer)stock.get("stockLocked"));
	        }
	        
        }
        
    }
    
    public Map<String, Object> selectStocksBy2Gids(Integer groupId, Integer goodsId) {
    	Map<String, Object> param = new HashMap<String, Object>();
        param.put("groupId", groupId);
        param.put("goodsId", goodsId);
        List<Map> ggs = sqlSession.selectList(namespace + ".selectStocksBy2Gids", param);
        return ggs.get(0);
    }
    
    public int updateStocksBy2Gids(Integer groupId, Integer goodsId, Integer stockSum, Integer stockLocked) {
    	Map<String, Object> param = new HashMap<String, Object>();
        param.put("groupId", groupId);
        param.put("goodsId", goodsId);//goods.getGoodsId()
        param.put("stockSum", stockSum);
        param.put("stockLocked", stockLocked);
        return sqlSession.update(namespace + ".updateStocksBy2Gids", param);
    }

    public void release(Integer groupId, String goodsId, Integer quantity) {
        Map<String, Object> param = new HashMap<>();
        param.put("goodsId", goodsId);
        param.put("groupId", groupId);
        param.put("quantity", quantity);
        sqlSession.update(namespace + ".updateStock", param);
    }
    

    /**
     * 查询商品已发布的所有网点
     * @param goodsId 商品ID
     * @return
     */
    public List<Map<String, Object>> selectGoodsGroup(int goodsId) {
        return sqlSession.selectList(namespace + ".selectGroupByGoods", goodsId);
    }

    /**
     * 查询网点已发布的所有商品
     * @param groupId
     * @return
     */
    public List<Map<String, Object>> selectGroupGoods(int groupId) {
        return sqlSession.selectList(namespace + ".selectGoodsByGroup", groupId);
    }
    
    /**
     * 商品发布保存，商品与网点关联关系
     * @param param
     * @return
     */
    public int saveGroupGoods(Map<String, Object> param) {
        sqlSession.delete(namespace + ".deleteGoodsGroup", param);
        if (param.get("type").equals("group")) {
            if (((Map[]) param.get("groups")).length <= 0) return 0;
        } else {
            if (((Map[]) param.get("goods")).length <= 0) return 0;
        }
        return sqlSession.insert(namespace + ".insertGoodsGroup", param);
    }

    /**
     * 网点关联所有商品
     * TODO 还有问题，网点列表只列出了当前用户的所有网点，而全部关联则是关联全部网点
     * @param param
     * @return
     */
    public int linkAllGoods(int param) {
        return sqlSession.insert(namespace + ".insertAllGoods", param);
    }

    /**
     * 商品关联所有网点
     * TODO 还有问题，网点列表只列出了当前用户的所有网点，而全部关联则是关联全部网点
     * @param param
     * @return
     */
    public int linkAllGroup(int param) {
        return sqlSession.insert(namespace + ".insertAllGroup", param);
    }

}
