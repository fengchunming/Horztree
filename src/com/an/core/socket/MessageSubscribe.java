package com.an.core.socket;

import com.an.sys.entity.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

/**
 * 消息发送
 * Created by karas on 10/16/14.
 */
@Controller
public class MessageSubscribe {
    private static final Logger logger = LoggerFactory
            .getLogger(MessageSubscribe.class);

    /**
     * 订阅信息示例
     */
    @SubscribeMapping("/notify")
    public Message outBound() throws Exception {
        Message msg = new Message();
        System.out.println("outBound:" + msg.getNote());
        return msg;
    }

    /**
     * 发送信息
     *
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/send")
    @SendTo("/notify")
    public Message inBound(Message message) throws Exception {
        message.setTitle("随使输点啥");
        message.setUser("karas");
        return message;
    }


    /**
     * 队列异常处理
     */
    @MessageExceptionHandler
    @SendToUser("/queue/errors")
    public String handleException(Throwable e) {
        logger.error(e.getMessage(), e);
        return e.getMessage();
    }
}
