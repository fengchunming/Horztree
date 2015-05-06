package com.an.sys.dao;

import com.an.core.model.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class OptionDao extends BaseDao<Map, String> {
    public OptionDao() {
        super();
        namespace = "PublicMapper";
    }

}
