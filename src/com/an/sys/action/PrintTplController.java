package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.PrintTplDao;
import com.an.sys.entity.PrintTpl;
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
 * 打印模板管理
 * <p/>
 * Create by Karas 2012-3-8
 */
@Controller
@RequestMapping("/sys")
public class PrintTplController {

    private static final Logger logger = LoggerFactory
            .getLogger(PrintTplController.class);

    @Autowired
    private PrintTplDao printDao;

    /**
     * 查询模块列表
     */
    @RequestMapping(value = "/printTpl/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        Collection<PrintTpl> prints = printDao.selectList(mParam);
        result.put("list", prints);
        result.put("count", printDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询打印模板
     */
    @RequestMapping(value = "/printTpl/{id}", method = RequestMethod.GET)
    public PrintTpl load(@PathVariable(value="id") int id) throws BadRequestException {
        return printDao.selectOne(id);
    }

    @RequestMapping(value = "/printTpl/byType/{type}", method = RequestMethod.GET)
    public PrintTpl load(@PathVariable(value="id") String type) throws BadRequestException {
        return printDao.selectByType(type);
    }

    /**
     * 更新打印模板
     */
    @RequestMapping(value = "/printTpl", method = RequestMethod.POST)
    public PrintTpl insert(@RequestBody PrintTpl print)
            throws BadRequestException {
        if (printDao.insert(print) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return print;
        }
    }

    /**
     * 更新打印模板
     */
    @RequestMapping(value = "/printTpl", method = RequestMethod.PUT)
    public PrintTpl update(@RequestBody PrintTpl print)
            throws BadRequestException {
        if (printDao.update(print) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return print;
        }
    }

    /**
     * 删除打印模板
     */
    @RequestMapping(value = "/printTpl/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id) throws BadRequestException {
        if (printDao.delete(id) <= 0)
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
