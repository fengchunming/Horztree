package com.an.cms.dao;

import com.an.cms.entity.Centrefold;
import com.an.core.model.BaseDao;
import org.springframework.stereotype.Repository;


@Repository
public class CentrefoldDao extends BaseDao<Centrefold, Integer> {
    public CentrefoldDao() {
        super();
        namespace = "CMS.CentrefoldMapper";
    }

}
