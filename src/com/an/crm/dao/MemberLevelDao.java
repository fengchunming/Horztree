package com.an.crm.dao;


import com.an.core.model.BaseDao;
import com.an.crm.entity.MemberLevel;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class MemberLevelDao extends BaseDao<MemberLevel, Integer> {
    public MemberLevelDao() {
        super();
        namespace = "CRM.MemberLevelMapper";
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectKV");
    }

}
