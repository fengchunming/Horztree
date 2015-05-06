package com.an.fm.dao;

import com.an.core.model.BaseDao;
import com.an.fm.entity.Tax;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class TaxDao extends BaseDao<Tax, Integer> {
    public TaxDao() {
        super();
        namespace = "TaxMapper";
    }


    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectTaxKV");
    }

}
