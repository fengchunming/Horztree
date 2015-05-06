package com.an.sys.service;

import com.an.sys.dao.MessageDao;
import com.an.sys.dao.MessageTplDao;
import com.an.sys.entity.Message;
import com.an.utils.Util;
import net.sf.json.JSONObject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 消息发送服务
 * Created by karas on 5/16/14.
 */
@Service
public class MessageService {

    private static final Logger logger = LoggerFactory
            .getLogger(MessageService.class);
    public static final String STATE_FINISHED = "2";
    public static final String STATE_PROCESS = "1";



    @Autowired
    private MessageTplDao messageTplDao;

    @Autowired
    private MessageDao messageDao;

    public void newNotify() {
//        mParam.put("privateStatus", "0");
//        mParam.put("status", "1");
//        System.out.println("karas:xxxx");
//        for (MessageChannel channel : SessionRepository.getUserSockets("1012")) {
//            System.out.println("prefix:"+ template.getUserDestinationPrefix());
//            System.out.println("sockid:" + sockId);
        Message msg = new Message();
        msg.setNote("time notify:" + Util.RunTimeSequence());
//        template.convertAndSend("/notify/karas", msg);
//        }
        //messageDao.countMyMessage(mParam);
    }

    /**
     * 加载模板或消息
     *
     * @param sceneId 场景Id 对应 category 消息分类
     * @param bizId   业务id 对应 业务表主键id
     * @return 消息列表
     */
    public List<Message> loadMessageOrTemplate(Integer sceneId, Integer bizId) {
        return messageDao.selectMessagesOrTemplate(sceneId, bizId);
    }

    /**
     * 获取消息模板
     *
     * @param code 消息代码
     * @return 消息模板
     */
    public Message getTemplate(String code) {
        Message msg = messageTplDao.selectByCode(code);
        if (msg == null) throw new RuntimeException("消息模板未定义");
        msg.setId(0);
        return msg;
    }

    /**
     * 发送、重发消息（修改消息）
     *
     * @param message 消息
     */
    public void send(Message message) {
        send(message, false);
    }


    /**
     * 发送，重发消息
     *
     * @param message     消息
     * @param forceResend 强制重发，if true 不修正原消息
     */
    @Transactional
    public void send(Message message, Boolean forceResend) {
        if (!forceResend && messageDao.exist(message)) {
            messageDao.update(message);
            logger.info("消息重发:" + message.getCode() + message.getNote());
        } else {
            if (message.getSendAt() == null) message.setSendAt(new Date());
            messageDao.insert(message);

            logger.info("消息发送:" + message.getCode() + message.getNote());
        }
        messageDao.sendTo(message);
        //for (Integer userId : message.getToUser())
//        template.convertAndSendToUser("karas", "/notify", message);
    }

    /**
     * 取消消息
     *
     * @param code  消息代码
     * @param bizId 业务id 对应 业务表主键id
     */
    public void trash(String code, Integer bizId) {
        Message msg = new Message();
        msg.setCode(code);
        msg.setBizId(bizId);
        msg.setStatus("d");
        messageDao.update(msg);
    }

    public void deal(String code, Integer bizId) {
        Message msg = new Message();
        msg.setCode(code);
        msg.setBizId(bizId);
        msg.setStatus(STATE_FINISHED);
        messageDao.update(msg);
    }

    /**
     * 指定消息类型发送消息
     *
     * @param code 消息类型（消息编码）
     * @param data 用以装载消息的对象
     */
    public Message getInstance(String code, Object data) {
        Message message = getTemplate(code);
        serial(message, data);
        stamp(message);
        return message;
    }

    /**
     * 通过对象发送消息，并指定回调参数
     *
     * @param message 消息体
     * @param param   回调参数
     */
    public Message putParam(Message message, Map<String, Object> param) {
        message.setParam(JSONObject.fromObject(param).toString());
        return message;
    }


    /**
     * 给消息加初始状态和时间戳
     *
     * @param msg 消息体
     * @return 消息
     */
    private Message stamp(Message msg) {
        msg.setStatus(MessageService.STATE_PROCESS);
        msg.setSendAt(new Date());
        //msg.setExpireAt();
        return msg;
    }

    /**
     * 装载消息，生成消息标题和内容
     *
     * @param msg  消息
     * @param data 对象
     * @return 反回装载返消息
     */
    private Message serial(Message msg, Object data) {
        VelocityContext context = new VelocityContext();
        context.put("data", data);
        StringWriter sw = new StringWriter();
        Velocity.evaluate(context, sw, "", msg.getNote());
        sw.flush();
        msg.setNote(sw.toString());
        return msg;
    }

}