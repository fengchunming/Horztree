package com.an.sys.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class MessageTplDao extends BaseDao<Message, Integer> {
    public MessageTplDao() {
        super();
        namespace = "SYS.MessageTemplateMapper";
    }

    public int save(Message entity) {
        if (entity.getId() != null && entity.getId() > 0) {
            return update(entity);
        } else {
            return insert(entity);
        }
    }

    public Message selectByCode(String code) {
        Map<String, Object> param = new HashMap<>();
        param.put("code", code);
        List<Message> rst = selectList(param);
        if (rst.size() > 0)
            return rst.get(0);
        return null;
    }
    
    public Integer isCodeExist(Message entity) {
		return sqlSession.selectOne(namespace + ".isCodeExist", entity);
	}
    
	public Collection<Map<String, String>> selectKV() {
		return sqlSession.selectList(namespace + ".selectTourMessageTypeKV");
	}
    
}
