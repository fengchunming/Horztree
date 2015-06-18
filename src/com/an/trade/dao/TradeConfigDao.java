package com.an.trade.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.Merchant;
import com.an.sys.entity.Module;
import com.an.sys.entity.Role;
import com.an.sys.entity.User;
import com.an.trade.entity.Trade;
import com.an.trade.entity.TradeConfig;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TradeConfigDao extends BaseDao<TradeConfig, Integer> {
    public TradeConfigDao() {
        super();
        namespace = "TradeConfigMapper";
    }
    
 
    

   

    

}
