package com.an.trade.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.trade.dao.ReturnReasonDao;
import com.an.trade.entity.ReturnReason;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * 后台系统功能模块管理
 *
 * @author Karas
 * @date 2012-3-8
 */
@Controller
@RequestMapping("/trade")
public class ReturnReasonController {

    private static final Logger logger = LoggerFactory
            .getLogger(ReturnReasonController.class);

    @Autowired
    private ReturnReasonDao returnReasonDao;


    @RequestMapping(value = "/returnReason/list", method = RequestMethod.GET)
    public Map<?, ?> selectList(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", returnReasonDao.selectList(mParam));
        result.put("count", returnReasonDao.count(mParam));
        return result;
    }


    @RequestMapping(value = "/returnReason/{id}", method = RequestMethod.GET)
    public ReturnReason selectOne(@PathVariable(value="id") int id)
            throws BadRequestException {
        return returnReasonDao.selectOne(id);
    }


    @RequestMapping(value = "/kv/returnReason.do", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> selectReturnReasonKV() throws BadRequestException {
        return returnReasonDao.selectKV();
    }


    @RequestMapping(value = "/returnReason", method = RequestMethod.POST)
    public ReturnReason insert(@RequestBody ReturnReason returnReason) throws BadRequestException {
        if (returnReasonDao.insert(returnReason) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return returnReason;
        }
    }

    @RequestMapping(value = "/returnReason/{id}", method = RequestMethod.PUT)
    public ReturnReason update(@RequestBody ReturnReason returnReason) throws BadRequestException {
        if (returnReasonDao.update(returnReason) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return returnReason;
        }
    }

    @RequestMapping(value = "/returnReason/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id)
            throws BadRequestException {
        if (returnReasonDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }


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
