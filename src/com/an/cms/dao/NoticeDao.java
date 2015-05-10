package com.an.cms.dao;

import com.an.cms.entity.Notice;
import com.an.core.model.BaseDao;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeDao extends BaseDao<Notice, Integer> {
    public NoticeDao() {
        super();
        namespace = "CMS.NoticeMapper";
    }

    public Notice selectInit(Integer primaryKey) {
        if (primaryKey != null && primaryKey > 0)
            return selectOne(primaryKey);
        else
            return new Notice();
    }

    public int save(Notice notice) {
        if (notice.getId() != null && notice.getId() > 0) {
            return update(notice);
        } else {
            return insert(notice);
        }
    }

    public int publish(Integer id) {
        Notice notice = selectOne(id);
        notice.setStatus("p");
        return sqlSession.update(namespace + ".updateState", notice);
    }
}