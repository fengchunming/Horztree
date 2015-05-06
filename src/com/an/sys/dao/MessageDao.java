package com.an.sys.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.Message;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
public class MessageDao extends BaseDao<Message, Integer> {
    public MessageDao() {
        super();
        namespace = "SYS.MessageMapper";
    }

    public List<Message> selectMyMessage(Map<String, ?> params) {
        return sqlSession.selectList(namespace + ".selectMyMessage", params);
    }

    public int countMyMessage(Map<String, ?> params) {
        return sqlSession.selectOne(namespace + ".countMyMessage",
                params);
    }

    public int updatePrivateStatus(Message message) {
        if (sqlSession.update(namespace + ".updatePrivateStatus", message) <= 0)
            return sqlSession.insert(namespace + ".insertPrivateStatus", message);
        return 1;
    }

    public List<Message> selectMessagesOrTemplate(Integer sceneId, Integer bizId) {
        Map<String, Object> param = new HashMap<>();
        param.put("sceneId", sceneId);
        param.put("bizId", bizId);
        return sqlSession.selectList(namespace + ".selectTemplateLeftJoinMessage", param);
    }

    public boolean exist(Message message) {
        Integer count = sqlSession.selectOne(namespace + ".countExists", message);
        message.setId(count);
        return message.getId() != null;
    }

    public List<Message> selectMessageListByBizId(String sceneId, Integer bizId) {
        Map<String, Object> param = new HashMap<>();
        param.put("sceneId", sceneId);
        param.put("bizId", bizId);
        return sqlSession.selectList(namespace + ".selectMessageListByBizId", param);
    }

    public int countByBizId(String sceneId, Integer bizId) {
        Map<String, Object> param = new HashMap<>();
        param.put("sceneId", sceneId);
        param.put("bizId", bizId);
        return sqlSession.selectOne(namespace + ".countByBizId", param);
    }

    public void sendTo(Message message) {
        sqlSession.insert(namespace + ".insertSendTo", message);
        sqlSession.delete(namespace + ".deletePrivateStatus", message);
    }
}
