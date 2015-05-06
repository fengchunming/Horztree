package com.an.crm.dao;

import com.an.core.model.BaseDao;
import com.an.crm.entity.Comment;
import org.springframework.stereotype.Repository;

@Repository
public class CommentDao extends BaseDao<Comment, Integer> {
    public CommentDao() {
        super();
        namespace = "CRM.CommentMapper";
    }

}