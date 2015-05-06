package com.an.fm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.fm.dao.ReportDao;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
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
@RequestMapping("/fi")
public class ReportController {

    private static final Logger logger = LoggerFactory
            .getLogger(ReportController.class);

    @Autowired
    private ReportDao reportDao;

    /**
     * 查询模块列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/saleReport", method = RequestMethod.GET)
    public Map<?, ?> saleReport(WebRequest request) throws BadRequestException {
        Map<String, Object> params = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", reportDao.saleReport(params));
        result.put("count", "");
        return result;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/purchaseReport", method = RequestMethod.GET)
    public Map<?, ?> purchaseReport(WebRequest request)
            throws BadRequestException {
        Map<String, Object> params = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", reportDao.purchaseReport(params));
        result.put("count", "");
        return result;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @param request
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/stockReport", method = RequestMethod.GET)
    public Map<?, ?> stockReport(WebRequest request)
            throws BadRequestException {
        Map<String, Object> params = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", reportDao.stockReport(params));
        result.put("count", reportDao.stockCount(params));
        return result;
    }

    @RequestMapping(value = "/issueReport", method = RequestMethod.GET)
    public Map<?, ?> issueReport(WebRequest request)
            throws BadRequestException {
        Map<String, Object> params = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("list", reportDao.issueReport(params));
        result.put("count", reportDao.issueCount(params));
        return result;
    }

    /**
     * 异常处理
     *
     * @param e
     * @param out
     * @return
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
