package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.trade.dao.TradeDao;
import com.an.trade.entity.Trade;
import com.an.wm.dao.WorkBillDao;
import com.an.wm.dao.WorkBillDetailDao;
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

import java.util.Collection;
import java.util.List;

/**
 * 收货管理
 *
 * @author Karas  2012-3-8
 */
@Controller
@RequestMapping("/wm")
public class ReceiptController {

    private static final Logger logger = LoggerFactory
            .getLogger(ReceiptController.class);

    @Autowired
    private TradeDao tradeDao;

    @Autowired
    private WorkBillDao billDao;

    @Autowired
    private WorkBillDetailDao detailDao;

    @Autowired
    private InventoryService stockService;


    /**
     * 采购收货
     *
     * @throws BadRequestException
     */
    @RequestMapping(value = "/purchaseReceipt/{billCode}", method = RequestMethod.POST)
    public void saveReceipts(@RequestBody Item items[],
                             @PathVariable("billCode") String billCode)
            throws BadRequestException {
        WorkBill bill;
        Trade origin = tradeDao.selectByCode(billCode);
        List<WorkBill> bills = billDao.selectByOrigin(billCode, "input");
        if (bills.size() == 0) {
            bill = billDao.selectOrInit(null, "SH");
            bill.setStatus("input");
            bill.setType("SH");
            bill.setOriginBill(origin);
//            bill.setTarget(origin.getPartyB());
//            bill.setOrg(origin.getPartyA());
            bill.setWarehouse(origin.getWarehouse());
            billDao.save(bill);
        } else {
            bill = bills.get(0);
        }

        for (Item item : items) {
            if ("check".equals(item.getStatus()))
                continue;
            if (item.getBill() == null)
                item.setBill(bill);
//            item.setBelongTo(origin.getPartyA());
//            item.setPartyB(origin.getPartyB());
//            item.setWarehouse(bill.getWarehouse());
            detailDao.save(item);
        }
        tradeDao.updateRealQuantity(billCode);
        if (origin.getDealStatus().equals("waiting")) {
            origin.setDealStatus("working");
            tradeDao.updateState(origin);
        }
    }

    /**
     * 采购收货
     *
     * @param billCode
     * @throws BadRequestException
     */
    @RequestMapping(value = "/checkReceiptByOrigin/{billCode}", method = RequestMethod.POST)
    public void checkReceiptByPurchase(@PathVariable("billCode") String billCode)
            throws BadRequestException {
        List<WorkBill> bills = billDao.selectByOrigin(billCode, "input");
        Collection<Item> items = detailDao.selectByOriginCode(billCode);
        for (Item item : items) {
            if (item.getStatus().equals("check"))
                continue;
            item.setRealQuantity(item.getPlanQuantity());
            item.setStockId(stockService.receipt(item).getStockId());
            item.setStatus("check");
            detailDao.save(item);
        }

        for (WorkBill bill : bills) {
            bill.setStatus("check");
            billDao.updateStatus(bill);
        }
    }

    /**
     * 采购收货打印
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/purchaseReceiptBillReport/{id}", method = RequestMethod.GET)
    public ModelAndView report(@PathVariable("id") int id)
            throws BadRequestException {
        ModelAndView mav = new ModelAndView("report/purchaseReceipt");
        Trade bill = tradeDao.selectOne(id);
        mav.addObject("bill", bill);
        Collection<Item> details = detailDao.selectByOriginCode(bill
                .getBillCode());
        mav.addObject("list", details);
        mav.addObject("count", details.size());
        return mav;
    }

    /**
     * 完成收货
     *
     * @param billCode
     * @throws BadRequestException
     */
    @RequestMapping(value = "/resolveReceipt/{billCode}", method = RequestMethod.GET)
    public void resolveReceipt(@PathVariable("billCode") String billCode)
            throws BadRequestException {
        Trade origin = tradeDao.selectByCode(billCode);
        origin.setDealStatus("resolved");
        tradeDao.updateState(origin);
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
