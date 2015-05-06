package com.an.mm.dao;

import com.an.core.model.BaseDao;
import com.an.mm.entity.Brand;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BrandDao extends BaseDao<Brand, Integer> {
    public BrandDao() {
        super();
        namespace = "MM.BrandMapper";
    }

    public List selectKV() {
        return sqlSession.selectList(namespace + ".selectKV");
    }
}