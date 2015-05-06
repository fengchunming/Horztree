package com.an.crm.dao;

import com.an.core.model.BaseDao;
import com.an.crm.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class MemberDao extends BaseDao<Member, Integer> {
    public MemberDao() {
        super();
        namespace = "CRM.MemberMapper";
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectKV");
    }
}
