package com.an.crm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.crm.dao.CouponCodeDao;
import com.an.crm.dao.CouponDao;
import com.an.crm.entity.Coupon;
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
public class CouponController {
    private static final Logger logger = LoggerFactory
            .getLogger(CouponController.class);

    @Autowired
    private CouponDao couponDao;
    @Autowired
    private CouponCodeDao couponCodeDao;

    @RequestMapping(value = "/coupon/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", couponDao.selectList(mParam));
        result.put("count", couponDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/coupon/{id}", method = RequestMethod.GET)
    public Coupon load(@PathVariable(value="id") Integer id)
            throws BadRequestException {
        return couponDao.selectOne(id);
    }

    @RequestMapping(value = "/coupon", method = RequestMethod.POST)
    public Coupon insert(@RequestBody @Valid Coupon coupon, Errors rst)
            throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        }

        if (couponDao.insert(coupon) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return coupon;
        }
    }

    @RequestMapping(value = "/coupon/{id}", method = RequestMethod.PUT)
    public Coupon update(@RequestBody @Valid Coupon coupon, Errors rst) throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (couponDao.update(coupon) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return coupon;
        }
    }

    @RequestMapping(value = "/coupon/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id) throws BadRequestException {
        if (couponDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }


    @RequestMapping(value = "/coupon/getCode", method = RequestMethod.GET)
    public Map<?, ?> queryCode(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", couponCodeDao.selectList(mParam));
        result.put("count", couponCodeDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/coupon/addCode", method = RequestMethod.POST)
    public Coupon addCode(@RequestParam Integer id, @RequestParam Integer num)
            throws BadRequestException {
        Coupon coupon = couponDao.selectOne(id);
        for (int i = 0; i < num; i++) {
            Coupon code = coupon.clone();
            code.setCode(couponCodeDao.getNum(4));
            code.setStatus("0");
            couponCodeDao.insert(code);
        }
        coupon.setTotalNum(coupon.getTotalNum() + num);
        couponDao.update(coupon);
        return coupon;
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
