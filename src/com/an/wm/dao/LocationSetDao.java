package com.an.wm.dao;


import com.an.core.model.BaseDao;
import com.an.wm.entity.LocationSet;
import org.springframework.stereotype.Repository;

@Repository
public class LocationSetDao extends BaseDao<LocationSet, Integer> {
    public LocationSetDao() {
        super();
        namespace = "LocationSetMapper";
    }


    public LocationSet selectOrInit(Integer code) {
        if (code != null && code > 0)
            return selectOne(code);
        else
            return new LocationSet();
    }

    public int save(LocationSet location) {
        if (location.getId() != null && location.getId() > 0) {
            return update(location);
        } else {
            return insert(location);
        }
    }

}
