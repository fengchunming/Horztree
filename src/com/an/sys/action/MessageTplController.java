package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.MessageTplDao;
import com.an.sys.entity.Message;
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
 * 消息模板
 */
@Controller
@RequestMapping("/sys")
public class MessageTplController {

    private static final Logger logger = Logger.getLogger(MessageTplController.class);

    @Autowired
    private MessageTplDao messageDao;

    /**
     * 查询列表
     */
    @RequestMapping(value = "/messageTpl/list", method = RequestMethod.GET)
    public Map<?, ?> search(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        Collection<Message> list = messageDao.selectList(mParam);
        result.put("list", list);
        result.put("count", messageDao.count(mParam));
        return result;
    }

    /**
     * 新增
     */
    @RequestMapping(value = "/messageTpl", method = RequestMethod.POST)
    public Message insert(@RequestBody Message entity) throws BadRequestException {
        int count = messageDao.isCodeExist(entity);
        if (count > 0) {
            throw new BadRequestException("保存失败！消息代码重复!");
        }
        if (messageDao.insert(entity) != 1) {
            throw new BadRequestException("保存失败！");
        }
        return entity;
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/messageTpl/{id}", method = RequestMethod.PUT)
    public Message update(@RequestBody Message entity) throws BadRequestException {
        if (messageDao.update(entity) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return entity;
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/messageTpl/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) throws BadRequestException {
        if (messageDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }

    /**
     * 通过主键ID查询功能详情
     */
    @RequestMapping(value = "/kv/messagetype", method = RequestMethod.GET)
    public Collection<Map<String, String>> selectKV() throws BadRequestException {
        return messageDao.selectKV();
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
