package com.an.wm.dao;

import com.an.core.model.BaseDao;
import com.an.wm.entity.MaterialUom;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class MaterialUomDao extends BaseDao<MaterialUom, String> {

    public MaterialUomDao() {
        super.namespace = "GoodsUnitMapper";
    }

    public MaterialUom selectOrInit(String primaryKey) {
        if (primaryKey == null || primaryKey.equals("0")) {
            return new MaterialUom();
        } else {
            return selectOne(primaryKey);
        }
    }

    public int save(MaterialUom goodsUnit) {
        if (goodsUnit.getId() != null && goodsUnit.getId() > 0) {
            return update(goodsUnit);
        } else {
            return insert(goodsUnit);
        }
    }

    public Collection<MaterialUom> selectByGoods(String goodsCode) {
        return sqlSession.selectList(super.namespace + ".selectByGoods",
                goodsCode);
    }

    public int countByGoods(String goodsCode) {
        return sqlSession.selectOne(super.namespace + ".countByGoods",
                goodsCode);
    }

}
