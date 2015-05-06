package com.an.pos.dao;

import com.an.core.model.BaseDao;
import com.an.pos.entity.StoreDiary;
import org.springframework.stereotype.Repository;

@Repository
public class StoreDiaryDao extends BaseDao<StoreDiary, Integer> {

    public StoreDiaryDao() {
        super();
        namespace = "StoreDiaryMapper";
    }


    public int save(StoreDiary storeDiary) {
        if (storeDiary.getId() != null && storeDiary.getId() > 0) {
            return update(storeDiary);
        } else {
            return insert(storeDiary);
        }
    }

}
