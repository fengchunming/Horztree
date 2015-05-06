package com.an.fm.dao;

import com.an.core.model.BaseDao;
import com.an.fm.entity.ShipAudit;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ShipAuditDao extends BaseDao<ShipAudit, Integer> {
    public ShipAuditDao() {
        super();
        namespace = "ShipAuditMapper";
    }

    public ShipAudit selectOrInit(int primaryKey) {
        if (primaryKey > 0)
            return selectOrInit(primaryKey);
        else
            return new ShipAudit();
    }

    public int save(ShipAudit check) {
        if (check.getId() != null && check.getId() > 0) {
            return update(check);
        } else {
            return insert(check);
        }
    }

    public int insertShip(Map bill) {
        return sqlSession.insert(namespace + ".insertShip", bill);
    }

    public Map<?, ?> sumcount(Map<?, ?> params) {
        return sqlSession.selectOne(namespace + ".sumByExample", params);
    }

    public int check(Integer[] ids) {
        return sqlSession.update(namespace + ".check", ids);
    }

    public int delete(Integer[] ids) {
        return sqlSession.update(namespace + ".delete", ids);

    }

    public int addElse() {
        return sqlSession.insert(namespace + ".addElse");

    }

}
