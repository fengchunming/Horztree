package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.trade.dao.TradeDetailDao;
import com.an.wm.dao.ResupplyDao;
import com.an.wm.entity.Item;
import com.an.wm.entity.WorkBill;
import com.an.wm.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.*;

/**
 * 门店补货（调拨）
 *
 * @author karas
 */
@Controller
@RequestMapping("/wb")
public class ResupplyController {

    private static final Logger logger = LoggerFactory
            .getLogger(ResupplyController.class);

    @Autowired
    private ResupplyDao resupplyDao;

    @Autowired
    private TradeDetailDao resupplyDetailDao;

    @Autowired
    private InventoryService stockService;

    @Autowired
    private IssueController issueService;


    /**
     * 保存补货明细
     *
     * @param items
     * @param id
     * @throws BadRequestException
     */
    @RequestMapping(value = "/resupplyDetail/{billCode}", method = RequestMethod.POST)
    public void saveResupplyDetail(@RequestBody Item items[],
                                   @PathVariable("id") int id)
            throws BadRequestException {
        WorkBill bill = resupplyDao.selectOne(id);
        for (Item item : items) {
            if (item.getId() == null && "trash".equals(item.getStatus())) {
                continue;
            }
            item.setBill(bill);
//            item.setPartyA(bill.getPartyA());
            item = stockService.lock(item);
            resupplyDetailDao.save(item);
        }
    }

    /**
     * 自动补货
     *
     * @param billCode
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/autoResupplyDetail/{billCode}", method = RequestMethod.GET)
    public Collection<Item> autoResupplyDetailList(
            @PathVariable("billCode") String billCode)
            throws BadRequestException {
        Calendar cal = new GregorianCalendar();
        cal.setTime(new Date());
        List<Item> rst = resupplyDao.autoResupply(cal.get(Calendar.DAY_OF_WEEK)
                - 1 + "");
        for (int i = 0; i < rst.size(); i++) {
            Item bag = rst.get(i);
            if (bag.getSummary().compareTo(bag.getSafeLine()) < 0
                    && bag.getOriginSummary().compareTo(new BigDecimal(0)) > 0) { // 当前库存小于最小库存
                bag.setPlanQuantity(bag.getSafeLine().subtract(
                        bag.getSummary()));
                if (bag.getPlanQuantity().compareTo(bag.getOriginSummary()) > 0)
                    bag.setPlanQuantity(bag.getOriginSummary());
            } else {
                rst.remove(i);
                i--;
            }
        }
        return rst;
    }

    /**
     * 审核补货单
     *
     * @param id
     * @throws BadRequestException
     */
    @RequestMapping(value = "/checkResupply/{id}", method = RequestMethod.POST)
    public void checkResupply(@PathVariable("id") int id)
            throws BadRequestException {
        WorkBill bill = resupplyDao.selectOne(id);
        if (bill.getStatus().equals("check")) {
            throw new BadRequestException("该配货单已确认!");
        }
//        issueService.makeIssue(bill, false);
        bill.setStatus("check");
//        resupplyDao.save(bill);
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
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
