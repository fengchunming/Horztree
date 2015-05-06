package com.an.pos.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.pos.dao.SaleBillDao;
import com.an.pos.dao.SaleDetailDao;
import com.an.pos.entity.SaleBill;
import com.an.utils.Util;
import com.an.wm.entity.Item;
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
public class SaleBillController {

    private static final Logger logger = LoggerFactory
            .getLogger(SaleBillController.class);

    @Autowired
    private SaleBillDao saleBillDao;

    @Autowired
    private SaleDetailDao saleDetailDao;

    /**
     * 查询模块列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/saleBillList", method = RequestMethod.GET)
    public Map<?, ?> selectSaleBills(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<String, Object>();
        Collection<SaleBill> saleBills = saleBillDao.selectList(mParam);
        result.put("list", saleBills);
        result.put("count", saleBillDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/saleBill/{id}", method = RequestMethod.GET)
    public SaleBill selectSaleBill(@PathVariable(value="id") String id)
            throws BadRequestException {
        return saleBillDao.selectOne(id);
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/saleBillDetail/{code}", method = RequestMethod.GET)
    public Map<?, ?> selectSaleDetail(@PathVariable(value="id") String code)
            throws BadRequestException {
        Map<String, Object> result = new HashMap<String, Object>();
        Collection<Item> details = saleDetailDao.selectByCode(code);
        result.put("list", details);
        result.put("count", details.size());
        return result;
    }

    /**
     * 更新功能
     *
     * @param saleBill
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/saleBill/{id}", method = RequestMethod.POST)
    public SaleBill saveSaleBill(@RequestBody SaleBill saleBill) throws BadRequestException {
        if (saleBillDao.save(saleBill) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return saleBill;
        }
    }

    /**
     * 删除功能
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/saleBill/{id}", method = RequestMethod.DELETE)
    public void deleteSaleBill(@PathVariable(value="id") String id)
            throws BadRequestException {
        if (saleBillDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
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
