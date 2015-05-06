package com.an.mm.dao;

import com.an.core.model.BaseDao;
import com.an.mm.entity.Spec;
import org.springframework.stereotype.Repository;

@Repository
public class SpecDao extends BaseDao<Spec, Integer> {
    public SpecDao() {
        super();
        namespace = "MM.SpecMapper";
    }
}