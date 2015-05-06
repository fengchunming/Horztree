package com.an.pos.dao;

import com.an.core.model.BaseDao;
import com.an.pos.entity.StoreNotice;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Map;

@Repository
public class StoreNoticeDao extends BaseDao<StoreNotice, Integer> {

    public StoreNoticeDao() {
        super();
        namespace = "StoreNoticeMapper";
    }


    public int save(StoreNotice storeNotice) {
        if (storeNotice.getId() > 0) {
            return update(storeNotice);
        } else {
            return insert(storeNotice);
        }
    }

    public Collection<Map<Integer, String>> selectKV() {
        return sqlSession
                .selectList(namespace + ".selectStoreNoticeKV");
    }
}
