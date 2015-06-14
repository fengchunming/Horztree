package com.an.pos.dao;

import com.an.core.model.BaseDao;
import com.an.wm.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class SaleDetailDao extends BaseDao<Item, Integer> {

    public SaleDetailDao() {
        super();
        namespace = "SaleDetailMapper";
    }


    public int save(Item line) {
//        if (selectBySeqNo(line.getOriginNo()) != null) {
//            return update(line);
//        } else {
//            return insert(line);
//        }
    	return 0;
    }

    public Collection<Item> selectByCode(String code) {
        return sqlSession.selectList(namespace + ".selectByCode", code);
    }

    public Item selectBySeqNo(String seqNo) {
        return sqlSession.selectOne(namespace + ".selectBySeqNo", seqNo);
    }
}
