package com.an.wm.dao;

import com.an.core.model.BaseDao;
import com.an.wm.entity.Location;
import com.an.wm.entity.PossibleStorage;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class LocationDao extends BaseDao<Location, Integer> {
    public LocationDao() {
        super();
        namespace = "LocationMapper";
    }

    public Location selectOrInit(Integer code) {
        if (code != null && code > 0)
            return selectOne(code);
        else
            return new Location();
    }

    public int save(Location location) {
        if (location.getId() != null && location.getId() > 0) {
            return update(location);
        } else {
            return insert(location);
        }
    }

    public Collection<Map<String, String>> selectKV() {
        return sqlSession.selectList(namespace + ".selectLocationKV");
    }

    public Collection<Map<String, String>> possibleLocation(
            PossibleStorage params) {
        return sqlSession.selectList(namespace + ".possibleLocation", params);
    }

    public Location selectByCode(String code) {
        return sqlSession.selectOne(namespace + ".selectByCode", code);
    }
}
