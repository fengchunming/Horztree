package com.an.fm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.fm.dao.TaxDao;
import com.an.fm.entity.Tax;
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
@RequestMapping("/fm")
public class TaxController {

    private static final Logger logger = LoggerFactory
            .getLogger(TaxController.class);

    @Autowired
    private TaxDao taxDao;


    /**
     * 查询模块列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/taxList", method = RequestMethod.GET)
    public Map<?, ?> selectTaxs(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", taxDao.selectList(mParam));
        result.put("count", taxDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/tax/{id}", method = RequestMethod.GET)
    public Tax selectTax(@PathVariable(value="id") int id) throws BadRequestException {
        return taxDao.selectOne(id);
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/loadTaxKV.do", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> selectTaxKV()
            throws BadRequestException {
        return taxDao.selectKV();
    }

    /**
     * 更新功能
     *
     * @param tax
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/tax", method = RequestMethod.POST)
    public Tax insert(@RequestBody Tax tax) throws BadRequestException {
        if (taxDao.insert(tax) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return tax;
        }
    }

    /**
     * 更新功能
     *
     * @param tax
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/tax/{id}", method = RequestMethod.POST)
    public Tax update(@RequestBody Tax tax) throws BadRequestException {
        if (taxDao.update(tax) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return tax;
        }
    }

    /**
     * 删除功能
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/tax/{id}", method = RequestMethod.DELETE)
    public void deleteTax(@PathVariable(value="id") int id) throws BadRequestException {
        if (taxDao.delete(id) <= 0)
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
