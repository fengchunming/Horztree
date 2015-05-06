package com.an.fm.dao;

import com.an.core.model.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ReportDao extends BaseDao<List<Map<?, ?>>, String> {

    public ReportDao() {
        super();
        namespace = "ReportMapper";
    }


    public List<Map<?, ?>> saleReport(Map<?, ?> params) {
        return sqlSession.selectList(namespace + ".saleReport", params);
    }

    public List<Map<?, ?>> purchaseReport(Map<?, ?> params) {
        return sqlSession.selectList(namespace + ".purchaseReport", params);
    }

    public List<Map<?, ?>> resurpplyReport(Map<?, ?> params) {
        return sqlSession.selectList(namespace + ".resurpplyReport", params);
    }

    public List<Map<?, ?>> stockReport(Map<?, ?> params) {
        return sqlSession.selectList(namespace + ".stockReport", params);
    }

    public Object stockCount(Map<String, Object> params) {
        return sqlSession.selectOne(namespace + ".stockCount", params);
    }

    public Object issueReport(Map<String, Object> params) {
        return sqlSession.selectList(namespace + ".issueReport", params);
    }

    public Object issueCount(Map<String, Object> params) {
        return sqlSession.selectOne(namespace + ".issueCount", params);
    }

}
