package com.an.cms.action;

import com.an.cms.dao.CentrefoldDao;
import com.an.cms.entity.Centrefold;
import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台系统功能模块管理
 *
 * @author Karas
 * @date 2012-3-8
 */
@Controller
@RequestMapping("/cms")
public class CentrefoldController {

    private static final Logger logger = LoggerFactory
            .getLogger(CentrefoldController.class);

    @Autowired
    private CentrefoldDao centrefoldDao;


    @RequestMapping(value = "/centrefold/list", method = RequestMethod.GET)
    public Map<?, ?> selectList(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", centrefoldDao.selectList(mParam));
        result.put("count", centrefoldDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/centrefold/{id}", method = RequestMethod.GET)
    public Centrefold selectOne(@PathVariable(value="id") int id) throws BadRequestException {
        return centrefoldDao.selectOne(id);
    }

    @RequestMapping(value = "/centrefold", method = RequestMethod.POST)
    public Centrefold insert(@RequestBody Centrefold centrefold) throws BadRequestException {
        if (centrefoldDao.insert(centrefold) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return centrefold;
        }
    }

    @RequestMapping(value = "/centrefold/{id}", method = RequestMethod.PUT)
    public Centrefold update(@RequestBody Centrefold centrefold) throws BadRequestException {
        if (centrefoldDao.update(centrefold) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return centrefold;
        }
    }


    @RequestMapping(value = "/centrefold/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id) throws BadRequestException {
        if (centrefoldDao.delete(id) <= 0)
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
