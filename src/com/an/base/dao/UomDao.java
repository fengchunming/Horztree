package com.an.base.dao;

import com.an.base.entity.Uom;
import com.an.core.model.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class UomDao extends BaseDao<Uom, Integer> {
    public UomDao() {
        super();
        namespace = "UomMapper";
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectKV");
    }

    public Uom selectByCode(String uomCode) {
        return sqlSession.selectOne(namespace + ".selectByCode", uomCode);
    }

    public Uom selectByName(String uomName) {
        return sqlSession.selectOne(namespace + ".selectByName", uomName);
    }

    public Uom save(Uom uom) {
        if (selectByName(uom.getName()) == null) {
            insert(uom);
        } else {
            update(uom);
        }
        return uom;
    }
}
