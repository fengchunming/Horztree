package com.an.mm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.SpecDao;
import com.an.mm.entity.Spec;
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

/**
 * 后台系统-商品规格管理
 *
 * @author Karas   2012-03-08
 */

@Controller
@RequestMapping("/mm")
public class SpecController {

    private static final Logger logger = LoggerFactory.getLogger(SpecController.class);

    @Autowired
    private SpecDao specDao;

    @RequestMapping(value = "/spec/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", specDao.selectList(mParam));
        result.put("count", specDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/spec/{id}", method = RequestMethod.GET)
    public Spec load(@PathVariable(value="id") Integer id) throws BadRequestException {
        return specDao.selectOne(id);
    }


    @RequestMapping(value = "/spec", method = RequestMethod.POST)
    public Spec insert(@RequestBody @Valid Spec spec, Errors rst) throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        }

        if (specDao.insert(spec) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return spec;
        }
    }

    @RequestMapping(value = "/spec/{id}", method = RequestMethod.PUT)
    public Spec update(@RequestBody @Valid Spec spec, Errors rst) throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (specDao.update(spec) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return spec;
        }

    }

    @RequestMapping(value = "/spec/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id) throws BadRequestException {
        if (specDao.delete(id) <= 0)
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
