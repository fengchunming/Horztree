package com.an.crm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.crm.dao.StrategyDao;
import com.an.crm.entity.Strategy;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/crm")
public class MktTaskController {
    private static final Logger logger = LoggerFactory
            .getLogger(MktTaskController.class);

    @Autowired
    private StrategyDao mktTaskDao;

    @RequestMapping(value = "/mktTask/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", mktTaskDao.selectList(mParam));
        result.put("count", mktTaskDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/mktTask/{id}", method = RequestMethod.GET)
    public Strategy load(@PathVariable(value="id") Integer id)
            throws BadRequestException {
        return mktTaskDao.selectOne(id);
    }

    @RequestMapping(value = "/mktTask", method = RequestMethod.POST)
    public Strategy insert(@RequestBody @Valid Strategy mktTask, Errors rst)
            throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        }

        if (mktTaskDao.insert(mktTask) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return mktTask;
        }
    }

    @RequestMapping(value = "/mktTask/{id}", method = RequestMethod.PUT)
    public Strategy update(@RequestBody @Valid Strategy mktTask, Errors rst) throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (mktTaskDao.update(mktTask) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return mktTask;
        }
    }

    @RequestMapping(value = "/mktTask/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id) throws BadRequestException {
        if (mktTaskDao.delete(id) <= 0)
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
