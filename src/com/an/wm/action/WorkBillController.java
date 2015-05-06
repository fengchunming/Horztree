package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.utils.Util;
import com.an.wm.dao.WorkBillDao;
import com.an.wm.dao.WorkBillDetailDao;
import com.an.wm.entity.Item;
import com.an.wm.entity.Location;
import com.an.wm.entity.WorkBill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用库存单据管理
 *
 * @author karas
 */
@Controller
@RequestMapping("/wb")
public class WorkBillController {
    private static final Logger logger = LoggerFactory.getLogger(WorkBillController.class);

    @Autowired
    private WorkBillDetailDao detailDao;

    @Autowired
    private WorkBillDao billDao;

    /**
     * 查询库存单据列表
     *
     * @param request  查询条件
     * @param billType 单据类型 IN-入库单，OUT-出库单
     */
    @RequestMapping(value = "/{billType}/list", method = RequestMethod.GET)
    public Map<?, ?> selectBillList(WebRequest request, @PathVariable("billType") String billType) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        mParam.put("billType", billType);
        Map<String, Object> result = new HashMap<>();
        result.put("list", billDao.selectList(mParam));
        result.put("count", billDao.count(mParam));
        return result;

    }

    /**
     * 查询单条单据
     *
     * @param id       单号
     * @param billType 单据类型
     */
    @RequestMapping(value = "/{billType}/{id}", method = RequestMethod.GET)
    public WorkBill loadBill(@PathVariable("id") int id, @PathVariable("billType") String billType) throws BadRequestException {
        return billDao.selectOne(id);
    }

    /**
     * 保存库存单据
     */
    @RequestMapping(value = "/{billType}", method = RequestMethod.POST)
    @Transactional
    public WorkBill insertBill(@RequestBody WorkBill bill) throws BadRequestException {
        if ("check".equals(bill.getStatus()))
            throw new BadRequestException("单据已确认！");
        if (billDao.save(bill) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            bill.getItems().stream().forEach(item -> saveDetail(item, bill));
            return bill;
        }
    }

    @RequestMapping(value = "/{billType}/{id}", method = RequestMethod.PUT)
    @Transactional
    public WorkBill updateBill(@RequestBody WorkBill bill) throws BadRequestException {
        if ("check".equals(bill.getStatus()))
            throw new BadRequestException("单据已确认！");
        if (billDao.save(bill) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            bill.getItems().stream().forEach(item -> saveDetail(item, bill));
            return bill;
        }
    }

    /**
     * 删除库存单据
     */
    @RequestMapping(value = "/{billType}/{id}", method = RequestMethod.DELETE)
    public void deleteBill(@PathVariable("id") int id)
            throws BadRequestException {
        if (billDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");

        // 删除明细
        detailDao.deleteByBill(id);
    }

    /**
     * 根据单号 查询库存单据明细
     */
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public Map<?, ?> selectDetails(WebRequest request,
                                   @PathVariable("id") int id)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        mParam.put("billId", id);
        Map<String, Object> result = new HashMap<>();
        result.put("list", detailDao.selectList(mParam));
        result.put("count", detailDao.count(mParam));
        return result;

    }

    /**
     * 根据来源单据（起始单据）查询库存单据明细
     */
    @RequestMapping(value = "/detailByOrigin/{billCode}", method = RequestMethod.GET)
    public Map<?, ?> selectDetailsByOrigin(
            @PathVariable("billCode") String billCode, WebRequest request)
            throws BadRequestException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> mParam = Util.GetRequestMap(request);
        mParam.put("originBill", billCode);
        result.put("list", detailDao.selectList(mParam));
        result.put("count", detailDao.count(mParam));
        return result;
    }


    /**
     * 按照指定模版打印单据
     *
     * @param view 指定模板
     * @param code 单据号
     * @throws BadRequestException
     */
    @RequestMapping(value = "/print/{view}/{code}", method = RequestMethod.GET)
    public ModelAndView print(@PathVariable("view") String view,
                              @PathVariable("code") int code) throws BadRequestException {
        ModelAndView mav = new ModelAndView("report/" + view);
        WorkBill bill = billDao.selectOne(code);
        mav.addObject("bill", bill);
        mav.addObject("list", detailDao.selectByBill(bill.getId()));
        mav.addObject("count", detailDao.countByBill(bill.getId()));
        return mav;
    }

    private void saveDetail(Item item, WorkBill bill) {
        item.setBillId(bill.getId());
        //item.setWarehouse(bill.getWarehouse());
        Location location = item.getLocation();
        if (location == null)
            location = bill.getFrom();
        item.setLocation(location);

        Location target = item.getTarget();
        if (target == null)
            target = bill.getTo();
        item.setTarget(target);
        //item.setPartyA(bill.getOrg());
        //item.setPartyB(bill.getTarget());
        item.setPlanDate(bill.getPlanDate());
        detailDao.save(item);
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
