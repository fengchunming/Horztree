package com.an.fm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.fm.dao.PaymentDao;
import com.an.fm.entity.Payment;
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
 * @author Karas  2012-3-8
 */
@Controller
@RequestMapping("/fm")
public class PaymentController {

    private static final Logger logger = LoggerFactory
            .getLogger(PaymentController.class);

    @Autowired
    private PaymentDao paymentDao;

    /**
     * 查询
     */
    @RequestMapping(value = "/payment/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        Collection<Payment> payments = paymentDao.selectList(mParam);
        result.put("list", payments);
        result.put("count", paymentDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询
     */
    @RequestMapping(value = "/payment/{id}", method = RequestMethod.GET)
    public Payment load(@PathVariable(value="id") int id) throws BadRequestException {
        return paymentDao.selectOne(id);
    }

    /**
     * 查询KV对应关系
     */
    @RequestMapping(value = "/kv/payment", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> selectKV()
            throws BadRequestException {
        return paymentDao.selectKV();
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/payment", method = RequestMethod.POST)
    public Payment insert(@RequestBody Payment payment) throws BadRequestException {
        if (paymentDao.insert(payment) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return payment;
        }
    }

    @RequestMapping(value = "/payment/{id}", method = RequestMethod.PUT)
    public Payment update(@RequestBody Payment payment) throws BadRequestException {
        if (paymentDao.update(payment) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return payment;
        }
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/payment/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id) throws BadRequestException {
        if (paymentDao.delete(id) <= 0)
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
