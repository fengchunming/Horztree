package com.an.sys.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.Organization;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class OrganizationDao extends BaseDao<Organization, Integer> {
    public OrganizationDao() {
        super();
        namespace = "OrganizationMapper";
    }

    public Organization selectInit(Integer id) {
        if (id != null && id > 0) {
            return selectOne(id);
        } else {
            return new Organization();
        }
    }

    @Transactional
    public int save(Organization org) {
        if (org.getId() != null && org.getId() > 0) {
            return update(org);
        } else {
            return insert(org);
        }
    }

    public Collection<Map<String, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectKV");
    }

    public Collection<Map<String, String>> selectKV(String orgType) {
        Map<String, Object> params = new HashMap<>();
        params.put("orgType", orgType);
        return sqlSession.selectList(namespace + ".selectKV", params);
    }

    public Organization selectByPartnerNo(String code) {
        Organization org = sqlSession.selectOne(namespace
                + ".selectByPartnerNo", code);
        if (org == null) {
            org = new Organization();
            org.setOrgCode(nextCode());
        }
        return org;
    }

    private String nextCode() {
        return String.format("%04d", sqlSession.selectOne(
                "PublicMapper.nextVal", "organization_seq"));
    }

    public int updateSync(Organization org) {
        return sqlSession.update(namespace + ".updateSync", org);
    }
}
