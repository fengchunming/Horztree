package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.MessageDao;
import com.an.sys.entity.Message;
import com.an.sys.service.MessageService;
import com.an.utils.Util;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息
 */
@Controller
@RequestMapping("/sys")
public class MessageController {

    private static final Logger logger = Logger.getLogger(MessageController.class);

    @Autowired
    private MessageDao messageDao;

    @Autowired
    private MessageService messageService;


    /**
     * 获取我的待办事项
     */

    @RequestMapping(value = "/message/list", method = RequestMethod.GET)
    public Map<?, ?> search(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        Collection<Message> list = messageDao.selectMyMessage(mParam);
        result.put("list", list);
        result.put("count", messageDao.countMyMessage(mParam));
        mParam.put("privateStatus", "0");
        mParam.put("status", "1");
        mParam.put("type", "n");
        result.put("notice", messageDao.countMyMessage(mParam));
        mParam.put("type", "t");
        result.put("task", messageDao.countMyMessage(mParam));
        return result;
    }

    @RequestMapping(value = "/checkMessage", method = RequestMethod.GET)
    public Map<?, ?> checkMessage(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        mParam.put("privateStatus", "0");
        mParam.put("status", "1");
        result.put("noread", messageDao.countMyMessage(mParam));

        return result;
    }

    /**
     * 新增消息
     */

    @RequestMapping(value = "/message", method = RequestMethod.POST)
    public Message save(@RequestBody Message entity) throws BadRequestException {
        //messageService.send(entity);
        return entity;
    }

    /**
     * 更新消息
     */

    @RequestMapping(value = "/message/{id}", method = RequestMethod.PUT)
    public Message update(@RequestBody Message entity) throws BadRequestException {
        //messageService.send(entity);
        return entity;
    }


    /**
     * 读消息
     */
    @RequestMapping(value = {"/readMessage/{id}"}, method = RequestMethod.GET)
    public void readMessage(@PathVariable(value="id") Integer id) throws BadRequestException {
        Message msg = new Message();
        msg.setId(id);
        msg.setPrivateStatus("1");
        if (messageDao.updatePrivateStatus(msg) != 1) {
            throw new BadRequestException("保存失败！");
        }
    }


    /**
     * 删除消息
     */
    @RequestMapping(value = "/message/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) throws BadRequestException {
        if (messageDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }


    /**
     * 异常处理
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequestException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView otherException(Exception e) {
        logger.warn(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
