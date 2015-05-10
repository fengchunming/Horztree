package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.wm.dao.InventoryLogDao;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 完成工作
 *
 * @author Karas 2012-3-8
 */
@Controller
@RequestMapping("/wm")
public class WorkDoneController {

    private static final Logger logger = LoggerFactory.getLogger(WorkDoneController.class);

    @Autowired
    private WorkBillDao billDao;

    @Autowired
    private WorkBillDetailDao detailDao;

    @Autowired
    private InventoryService stockService;

    @Autowired
    private InventoryLogDao logDao;
    
    /**
     * 进货单据确认
     * @param id
     * @throws BadRequestException
     */
    @RequestMapping(value = "/checkReceipt/{billId}", method = RequestMethod.GET)
    @Transactional
    public void checkReceiptBill(@PathVariable("billId") int id) throws BadRequestException {
        WorkBill bill = billDao.selectOne(id);
        Collection<Item> items = detailDao.selectByBill(id);
        for (Item item : items) {
			if (item.getStatus().equals("1")) {//已经确认过的，不再……
				continue;
			}
            //item.setGroupId(item.getTarget().getWarehouse());
            item.setBill(bill);
            item.setStockId(stockService.receipt(item).getStockId());
            item.setStatus("1");
            detailDao.save(item);
        }
        bill.setStatus("1");
        billDao.updateStatus(bill);
    }

    @RequestMapping(value = "/checkIssue/{billCode}", method = RequestMethod.GET)
    @Transactional
    public void checkIssueBill(@PathVariable("billCode") int id) throws BadRequestException {
        WorkBill bill = billDao.selectOne(id);
        if (bill.getStatus().equals("1"))
            throw new BadRequestException("发货单已确认！");

        bill.setStatus("1");
        bill.setRealDate(new Date());
//        bill.setDealBy(dealBy);

        Collection<Item> items = detailDao.selectByBill(id);
        for (Item item : items) {
            if (item.getStatus().equals("1"))
                continue;
            stockService.issue(item);
            item.setStatus("1");
            detailDao.update(item);
        }
        billDao.updateStatus(bill);
    }

    /**
     * 调拨单确认
     * @param billCode
     * @throws BadRequestException
     */
    @RequestMapping(value = "/checkTransit/{billCode}", method = RequestMethod.GET)
    @Transactional
    public void checkTransit(@PathVariable("billCode") int billCode) throws BadRequestException {
        WorkBill bill = billDao.selectOne(billCode);
        if ("1".equals(bill.getStatus())) {
            throw new BadRequestException("调拨单已确认!");
        }

        Collection<Item> items = detailDao.selectByBill(billCode);
        for (Item item : items) {
            if (item.getStatus().equals("1"))
                continue;
            stockService.transit(item);
            item.setStatus("1");
            detailDao.save(item);
        }

        bill.setStatus("1");
        billDao.save(bill);
    }
    /**
     * 移库单
     *
     * @param billCode
     */
    @RequestMapping(value = "/checkMove/{billCode}", method = RequestMethod.GET)
    public void checkMove(@PathVariable("billCode") int billCode) throws BadRequestException {
        WorkBill bill = billDao.selectOne(billCode);
        if ("1".equals(bill.getStatus())) {
            throw new BadRequestException("移库单已确认!");
        }

        Collection<Item> items = detailDao.selectByBill(billCode);
        for (Item item : items) {
            if (item.getStatus().equals("1"))
                continue;
            item.setRealQuantity(item.getPlanQuantity());
            item.setStatus("1");
            stockService.move(item);
            detailDao.save(item);
        }

        bill.setStatus("1");
        billDao.save(bill);
    }

    /**
     * 生成上架单并 保存上架明细
     */
    @RequestMapping(value = "/saveOnshelfDetails/{billCode}", method = RequestMethod.POST)
    public void saveOnshelfDetails(@RequestBody Item items[], @PathVariable("billCode") String billCode) throws BadRequestException {
        WorkBill bill;
        WorkBill origin = billDao.selectByCode(billCode);
        List<WorkBill> bills = billDao.selectByOrigin(billCode, "input");
        if (bills.size() == 0) {
            bill = billDao.selectOrInit(null, "SJ");
            bill.setStatus("input");
            bill.setType("SJ");
            bill.setOriginBill(origin);
            billDao.save(bill);
        } else {
            bill = bills.get(0);
        }

        for (Item item : items) {
            if ("check".equals(item.getStatus()))
                continue;
            item.setBill(bill);
//            item.setWarehouse(bill.getWarehouse());
            detailDao.save(item);
        }
        detailDao.updateRealQuantity(billCode);
    }

    /**
     * 上架单审核
     */
    @RequestMapping(value = "/checkOnshelf/{billCode}", method = RequestMethod.POST)
    public void checkOnshelf(@PathVariable("billCode") String billCode)
            throws BadRequestException {
        List<WorkBill> bills = billDao.selectByOrigin(billCode, "input");
        Collection<Item> items = detailDao.selectByOriginCode(billCode);
        for (Item item : items) {
            if (item.getStatus().equals("check"))
                continue;
            item.setRealQuantity(item.getPlanQuantity());
            item.setStockId(stockService.move(item).getStockId());
            item.setStatus("check");
            detailDao.save(item);
        }

        for (WorkBill bill : bills) {
            if (!bill.getStatus().equals("check"))
                continue;
            bill.setStatus("check");
            billDao.updateStatus(bill);
        }
    }

    /**
     * 分拣单
     */
    @RequestMapping(value = "/checkPickingBill/{billCode}", method = RequestMethod.POST)
    public WorkBill checkWmBillBill(@PathVariable("billCode") int billCode)
            throws BadRequestException {

        WorkBill bill = billDao.selectOne(billCode);
        if (bill.getStatus().equals("check"))
            throw new BadRequestException("发货单已确认！");
        bill.setStatus("check");

        if (billDao.save(bill) != 1) {
            throw new BadRequestException("确认失败！");
        } else {
            Collection<Item> items = detailDao.selectByBill(billCode);
            for (Item item : items) {
                if (item.getStatus().equals("check"))
                    continue;
                item.setId(0);
                stockService.issue(item);
            }
            return bill;
        }
    }

    /**
     * 废弃单
     *
     * @param billCode
     */
    @RequestMapping(value = "/checkTrash/{billCode}", method = RequestMethod.POST)
    public void checkTrash(@PathVariable("billCode") int billCode)
            throws BadRequestException {
        WorkBill bill = billDao.selectOne(billCode);
        if ("check".equals(bill.getStatus())) {
            throw new BadRequestException("废弃单已确认!");
        }

        Collection<Item> items = detailDao.selectByBill(billCode);
        for (Item item : items) {
            if (item.getStatus().equals("check"))
                continue;
            item.setStatus("check");
            stockService.trash(item);
            detailDao.save(item);
        }

        bill.setStatus("check");
        billDao.save(bill);
    }


    /**
     * 盘点
     *
     * @param billCode
     * @throws BadRequestException
     */
    @RequestMapping(value = "/linkInventory/{billCode}", method = RequestMethod.POST)
    public void linkInventory(@PathVariable("billCode") int billCode)
            throws BadRequestException {
        WorkBill bill = billDao.selectOne(billCode);
        if ("check".equals(bill.getStatus())) {
            throw new BadRequestException("盘点单已确认!");
        }

        Collection<Item> items = detailDao.selectByBill(billCode);
        for (Item item : items) {
            // 查询stockSnap 相应时间、库位、
            List<Item> co = logDao.selectByTimestamp(item, bill.getRealDate());
            //detailDao.save(item);
        }
    }

    /**
     * 盘点
     *
     * @param billCode
     * @throws BadRequestException
     */
    @RequestMapping(value = "/checkInventory/{billCode}", method = RequestMethod.POST)
    public void checkInventory(@PathVariable("billCode") int billCode)
            throws BadRequestException {
        WorkBill bill = billDao.selectOne(billCode);
        if ("check".equals(bill.getStatus())) {
            throw new BadRequestException("盘点单已确认!");
        }

        Collection<Item> items = detailDao.selectByBill(billCode);
        for (Item item : items) {
            if (item.getStatus().equals("check"))
                continue;
            item.setStatus("check");
            stockService.inventory(item);
            detailDao.save(item);
        }

        bill.setStatus("check");
        billDao.save(bill);
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
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
