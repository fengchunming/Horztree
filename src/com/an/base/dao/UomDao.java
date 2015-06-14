package com.an.base.dao;

import com.an.base.entity.Uom;
import com.an.core.model.BaseDao;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class UomDao extends BaseDao<Uom, Integer> {
	public UomDao() {
		super();
		namespace = "UomMapper";
	}

	public List<Map<String, Object>> selectKV() {
		return sqlSession.selectList(namespace + ".selectKV");
	}

	public Uom save(Uom uom) {
		if (uom.getId() == null || uom.getId() <=0) {
			insert(uom);
		} else {
			update(uom);
		}
		return uom;
	}
}
