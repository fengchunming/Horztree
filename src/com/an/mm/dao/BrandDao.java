package com.an.mm.dao;

import com.an.core.model.BaseDao;
import com.an.mm.entity.Brand;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class BrandDao extends BaseDao<Brand, Integer> {
	public BrandDao() {
		super();
		namespace = "MM.BrandMapper";
	}

	public List<Map<String, Object>> selectKV() {
		return sqlSession.selectList(namespace + ".selectKV");
	}
}