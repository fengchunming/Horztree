package com.an.crm.dao;

import com.an.core.model.BaseDao;
import com.an.crm.entity.Feedback;
import org.springframework.stereotype.Repository;

@Repository
public class FeedbackDao extends BaseDao<Feedback, Integer> {
    public FeedbackDao() {
        super();
        namespace = "CRM.FeedbackMapper";
    }

}