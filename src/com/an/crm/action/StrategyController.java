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
public class StrategyController {
    private static final Logger logger = LoggerFactory
            .getLogger(StrategyController.class);

    @Autowired
    private StrategyDao strategyDao;

    @RequestMapping(value = "/strategy/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", strategyDao.selectList(mParam));
        result.put("count", strategyDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/strategy/{id}", method = RequestMethod.GET)
    public Strategy load(@PathVariable(value="id") Integer id)
            throws BadRequestException {
        return strategyDao.selectOne(id);
    }

    @RequestMapping(value = "/strategy", method = RequestMethod.POST)
    public Strategy insert(@RequestBody @Valid Strategy strategy, Errors rst)
            throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        }

        if (strategyDao.insert(strategy) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            strategyDao.saveGroups(strategy);
            return strategy;
        }
    }

    @RequestMapping(value = "/strategy/{id}", method = RequestMethod.PUT)
    public Strategy update(@RequestBody @Valid Strategy strategy, Errors rst) throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (strategyDao.update(strategy) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            strategyDao.saveGroups(strategy);
            return strategy;
        }
    }


    @RequestMapping(value = "/clause", method = RequestMethod.GET)
    public Object loadClause(@RequestParam String className) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Class clause = Class.forName("com.an.crm.clause."+ className);
        return clause.newInstance();
    }

    @RequestMapping(value = "/strategy/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id) throws BadRequestException {
        if (strategyDao.delete(id) <= 0)
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
