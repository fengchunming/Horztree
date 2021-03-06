package com.an.wm.dao;

import com.an.core.model.BaseDao;
import com.an.mm.entity.WorkBill;
import com.an.wm.entity.Item;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ResupplyDao extends BaseDao<WorkBill,Integer> {

    public List<Item> autoResupply(String week) {
        return sqlSession.selectList("SmartSuggest.suggestResupply", week);
    }


}
