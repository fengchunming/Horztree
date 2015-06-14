package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.WorkBillDao;
import com.an.mm.dao.WorkBillDetailDao;
import com.an.mm.entity.WorkBill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


/**
 * 采购单管理
 *
 * @author karas
 */
@Controller
@RequestMapping("/wb")
public class PurchaseController {

    private static final Logger logger = LoggerFactory
            .getLogger(PurchaseController.class);

    @Autowired
    private WorkBillDetailDao detailDao;

    @Autowired
    private WorkBillDao billDao;

    /**
     * 审核采购单，审核完成后收货中可见
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/checkPurchase/{id}", method = RequestMethod.POST)
    public WorkBill checkPurchaseOrder(@PathVariable("id") int id)
            throws BadRequestException {
        WorkBill bill = billDao.selectOne(id);
        if (!bill.getStatus().equals("input"))
            throw new BadRequestException("采购单状态不满足确认条件！");
        bill.setStatus("1");
        if (billDao.updateDealStatus(bill) < 1) {
            throw new BadRequestException("确认失败！");
        } else {
            return bill;
        }
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
