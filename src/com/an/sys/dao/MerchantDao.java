package com.an.sys.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.Merchant;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class MerchantDao extends BaseDao<Merchant, Integer> {
    public MerchantDao() {
        super();
        namespace = "MerchantMapper";
    }

    public Merchant selectByCode(String code) {
        return sqlSession.selectOne(namespace + ".selectByCode", code.trim());
    }

    public Merchant selectByName(String name) {
        return sqlSession.selectOne(namespace + ".selectByName", name.trim());
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectKV");
    }

    public Collection<Map<Integer, String>> selectTemplateKV() {
        return sqlSession.selectList(namespace + ".selectTemplateKV");
    }

    /**
     * 生成下一个商户编号
     *
     * @return 商户序号
     */
    public String nextMerchantCode() {
        return "1" + String.format("%05d", sqlSession.selectOne(
                "PublicMapper.nextVal", "1001_merchant_code_seq"));
    }
}
