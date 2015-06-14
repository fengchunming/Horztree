package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.WorkBillDao;
import com.an.mm.dao.WorkBillDetailDao;
import com.an.mm.entity.WorkBill;
import com.an.sys.dao.OrganizationDao;
import com.an.trade.dao.TradeBillDetailDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 发货管理
 *
 * @author Karas  2012-3-8
 */
@Controller
@RequestMapping("/wm")
public class IssueController {

    private static final Logger logger = LoggerFactory.getLogger(IssueController.class);

    @Autowired
    private WorkBillDao billDao;

    @Autowired
    private TradeBillDetailDao tradeBillDetailDao;

    @Autowired
    private WorkBillDetailDao detailDao;

    @Autowired
    private OrganizationDao orgDao;

    @RequestMapping(value = "/autoIssue", method = RequestMethod.GET)
    public void issue() throws BadRequestException {
        Map<String, Object> mParam = new HashMap<>();
        mParam.put("billType", "FH");
        mParam.put("status", "input");
        Collection<WorkBill> issues = billDao.selectList(mParam);
        for (WorkBill bill : issues) {
           // checkIssue(bill.getId(), 0);
        }
    }

    /**
     * 生成发货单
     *
     * @param trade   交易订单
     * @param setReal 是否填写实发数量
     *//*
    public WorkBill makeIssue(Trade trade, boolean setReal)
            throws BadRequestException {

        Collection<Item> details = tradeDetailDao.selectByBill(trade
                .getId());
        if (billDao.selectByOrigin(trade.getBillCode(), null).size() > 0)
            return null;
        Map<String, Object> params = new HashMap<>();
        if (trade.getShipCode() == null || trade.getShipCode().isEmpty()) {
            params.put("originBill", trade.getBillCode());
        } else {
            params.put("shipCode", trade.getShipCode());
//            params.put("target", trade.getPartyA().getOrgCode());
//            params.put("orgCode", trade.getPartyB().getOrgCode());
        }

        WorkBill issue = billDao.selectByExample(params);
        Organization partyB = null;
        Organization partyA = new Organization();
        for (Item item : details) {
            if (issue == null
                    || !item.getPartyA().getOrgCode()
                    .equals(issue.getTarget().getOrgCode())) {
                issue = billDao.selectOrInit(null, "FH");
                issue.setBillDate(trade.getBillDate());
                issue.setPlanDate(trade.getPlanDate() == null ? new Date() : trade.getPlanDate());
                issue.setOriginBill(trade);
                issue.setBillCode(issue.getBillCode());
//                partyB = orgDao.selectOne(trade.getPartyB().getId());
//                issue.setWarehouse(partyB.getWarehouse());
//                partyA = trade.getPartyA();
                issue.setTarget(partyA);
                issue.setShipment(trade.getShipment());
                issue.setShipCode(trade.getShipCode());
                issue.setItemTotal(trade.getAmount());
                issue.setBelong(partyB);
                issue.setRemark(trade.getRemark());
                issue.setDealStatus("waiting");
                issue.setStatus("input");
                billDao.save(issue);
            } else {
                String origin = issue.getOriginBill().getBillCode();
//                partyB = orgDao.selectOne(trade.getPartyB().getId());
                if (!origin.contains(trade.getBillCode())) {
                    issue.getOriginBill().setBillCode(
                            origin + "," + trade.getBillCode());
                    billDao.save(issue);
                }
            }
//            item.setPartyB(trade.getPartyA());
//            item.setPa(partyB);
            Location loc = new Location();
//            loc.setSectionCode(partyB.getWarehouse().getOrgCode());
            item.setLocation(loc);
            item.setBill(issue);
            item.setBatchCode("--");
            item.setBatchAttr(new JSONObject());
            item.setAdjustQuantity(new BigDecimal(0));
            if (setReal)
                item.setRealQuantity(item.getPlanQuantity());
            item.setId(null);
            item.setOriginId(item.getId());
            detailDao.save(item);
        }

        return issue;
    }
*/

    /**
     * 单据打印
     *
     * @param ids
     * @throws BadRequestException
     */
    @RequestMapping(value = "/issue/printTrade/{ids}", method = RequestMethod.GET)
    public ModelAndView printTrade(@PathVariable("ids") String ids) throws BadRequestException {
        ModelAndView mav = new ModelAndView("report/pickings");
        Map<String, Object> param = new HashMap<>();

        if (ids.length() > 0) {
            param.put("billCodes", ids.split("-"));
        } else {
            param.put("status", "input");
            param.put("printStatus", 2);
        }
        Collection<WorkBill> bills = billDao.selectList(param);
        for (WorkBill bill : bills) {
            bill.setDetails(detailDao.selectByBill(bill.getId()));
            //bill.setPrintStatus(bill.getPrintStatus() | 2);
            if ("input".equals(bill.getStatus()))
                bill.setStatus("working");
            billDao.updateDealStatus(bill);
        }
        mav.addObject("trades", bills);
        return mav;
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
