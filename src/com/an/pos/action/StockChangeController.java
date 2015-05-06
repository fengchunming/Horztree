package com.an.pos.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.pos.dao.StockChangeBillDao;
import com.an.pos.dao.StockChangeDao;
import com.an.pos.entity.StockChange;
import com.an.pos.entity.StockChangeBill;
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
@RequestMapping("/pos")
public class StockChangeController {

    private static final Logger logger = LoggerFactory
            .getLogger(StockChangeController.class);

    @Autowired
    private StockChangeBillDao changeDao;

    @Autowired
    private StockChangeDao detailDao;

    /**
     * 查询模块列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/stockChangeBillList/{billType}", method = RequestMethod.GET)
    public Map<?, ?> selectStockChangeBills(WebRequest request,
                                            @PathVariable(value="billType") String billType) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<String, Object>();
        mParam.put("billType", billType);
        Collection<StockChangeBill> stockChanges = changeDao.selectList(mParam);
        result.put("list", stockChanges);
        result.put("count", changeDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/stockChangeList/{billCode}", method = RequestMethod.GET)
    public Map<?, ?> selectStockChanges(@PathVariable(value="billCode") String billCode,
                                        WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        mParam.put("billCode", billCode);
        Collection<StockChange> stockChanges = detailDao.selectList(mParam);
        result.put("list", stockChanges);
        result.put("count", detailDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/stockChangeBill/{id}", method = RequestMethod.GET)
    public StockChangeBill selectStockChange(@PathVariable(value="id") String id)
            throws BadRequestException {
        return changeDao.selectOne(id);
    }

    /**
     * 异常处理
     *
     * @param e
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
