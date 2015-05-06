package com.an.pos.dao;

import com.an.core.model.BaseDao;
import com.an.pos.entity.StockChange;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class StockChangeDao extends BaseDao<StockChange, Integer> {

    public StockChangeDao() {
        super();
        namespace = "StockChangeMapper";
    }

    public int save(StockChange line) {
        if (seqNoUnique(line.getClientSeqNo()) > 0) {
            return update(line);
        } else {
            return insert(line);
        }
    }

    public int seqNoUnique(String seqNo) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("clientSeqNo", seqNo);
        return sqlSession.selectOne(namespace + ".countByExample", params);
    }

}
