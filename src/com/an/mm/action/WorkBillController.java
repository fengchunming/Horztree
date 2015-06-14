package com.an.mm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.GoodsDao;
import com.an.mm.dao.WorkBillDao;
import com.an.mm.dao.WorkBillDetailDao;
import com.an.mm.entity.WorkBill;
import com.an.mm.entity.WorkBillDetail;
import com.an.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用库存单据管理
 *
 * @author karas
 */
@Controller
@RequestMapping("/mm")
public class WorkBillController {
	private static final Logger logger = LoggerFactory.getLogger(WorkBillController.class);

	@Autowired
	private WorkBillDao billDao;

	@Autowired
	private WorkBillDetailDao detailDao;
	
	@Autowired
	private GoodsDao goodsDao;

	/**
	 * 查询库存单据列表
	 * 
	 * @param request
	 * @param billType
	 *            单据类型 ：IN-入库单，OT-出库单，TF-调拨单
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/workbill/list", method = RequestMethod.GET)
	public Map<?, ?> selectBillList(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", billDao.selectList(mParam));
		result.put("count", billDao.count(mParam));
		return result;
	}

	/**
	 * 查询单据详细
	 * 
	 * @param id
	 * @param billType
	 *            单据类型 ：IN-入库单，OUT-出库单，TF-调拨单
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/workbill/{id}", method = RequestMethod.GET)
	public WorkBill loadBill(@PathVariable("id") int id) throws BadRequestException {
		return billDao.selectOne(id);
	}

	/**
	 * 保存库存单据
	 */
	@RequestMapping(value = "/workbill", method = RequestMethod.POST)
	@Transactional
	public WorkBill insertBill(@RequestBody WorkBill bill) throws BadRequestException {
		if (billDao.save(bill) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			for (WorkBillDetail detail : bill.getDetails()) {
				detail.setBillId(bill.getId());
				detailDao.save(detail);
			}
			return bill;
		}
	}

	@RequestMapping(value = "/workbill/{id}", method = RequestMethod.PUT)
	@Transactional
	public WorkBill updateBill(@RequestBody WorkBill bill) throws BadRequestException {
		if (billDao.save(bill) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			//TODO 删除的明细项没处理！！
			for (WorkBillDetail detail : bill.getDetails()) {
				detail.setBillId(bill.getId());
				detailDao.save(detail);
			}
			return bill;
		}
	}

	/**
	 * 删除库存单据
	 */
	@RequestMapping(value = "/workbill/{id}", method = RequestMethod.DELETE)
	@Transactional
	public void deleteBill(@PathVariable("id") int id) throws BadRequestException {
		if (billDao.delete(id) <= 0)
			throw new BadRequestException("删除失败");

		// 删除明细
		detailDao.deleteByBill(id);
	}

	/**
	 * 查询库存单据明细
	 */
	@RequestMapping(value = "/workbill/details/{billId}", method = RequestMethod.GET)
	public Map<?, ?> selectDetails(WebRequest request, @PathVariable("billId") int billId) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		mParam.put("billId", billId);
		Map<String, Object> result = new HashMap<>();
		result.put("list", detailDao.selectList(mParam));
		result.put("count", detailDao.count(mParam));
		return result;
	}

	/**
	 * 按照指定模版打印单据
	 *
	 * @param view
	 *            指定模板
	 * @param code
	 *            单据号
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/workbill/print/{view}/{code}", method = RequestMethod.GET)
	public ModelAndView print(@PathVariable("view") String view, @PathVariable("code") int code) throws BadRequestException {
		ModelAndView mav = new ModelAndView("report/" + view);
		WorkBill bill = billDao.selectOne(code);
		mav.addObject("bill", bill);
		mav.addObject("list", detailDao.selectByBill(bill.getId()));
		mav.addObject("count", detailDao.countByBill(bill.getId()));
		return mav;
	}

	/**
	 * 入库单确认
	 * @param billId
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/workbill/checkReceipt/{id}", method = RequestMethod.GET)
	@Transactional
	public void checkReceiptBill(@PathVariable("id") int id) throws BadRequestException {
		WorkBill bill = billDao.selectOne(id);
		if ("1".equals(bill.getDealStatus())) {
			throw new BadRequestException("入库单已确认，不可重复确认！");
		}
		List<WorkBillDetail> details = detailDao.selectByBill(id);
		Integer from = bill.getFromRegion();
		Integer to = bill.getToRegion();
		for (WorkBillDetail detail : details) {
			if (from != null) {
				goodsDao.reduceStock(detail.getGoodsId(), from, detail.getQuantity().intValue());
			}
			if (to != null) {
				goodsDao.addStock(detail.getGoodsId(), to, detail.getQuantity().intValue());
			}
		}
		bill.setDealStatus("1");
		bill.setDealTime(new Date());
		billDao.updateDealStatus(bill);
	}

	/**
	 * 出库确认
	 * @param billId
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/workbill/checkIssue/{billId}", method = RequestMethod.GET)
	@Transactional
	public void checkIssueBill(@PathVariable("billId") int billId) throws BadRequestException {
		WorkBill bill = billDao.selectOne(billId);
		if ("1".equals(bill.getDealStatus())) {
			throw new BadRequestException("出库单已确认，不可重复确认！");
		}

		List<WorkBillDetail> details = detailDao.selectByBill(billId);
		Integer from = bill.getFromRegion();
		Integer to = bill.getToRegion();
		for (WorkBillDetail detail : details) {
			if (from != null) {
				goodsDao.reduceStock(detail.getGoodsId(), from, detail.getQuantity().intValue());
			}
			if (to != null) {
				goodsDao.addStock(detail.getGoodsId(), to, detail.getQuantity().intValue());
			}
		}

		bill.setDealStatus("1");
		bill.setDealTime(new Date());
		billDao.updateDealStatus(bill);
	}

	/**
	 * 调拨确认
	 * @param billId
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/workbill/checkTransit/{billId}", method = RequestMethod.GET)
	@Transactional
	public void checkTransit(@PathVariable("billId") int billId) throws BadRequestException {
		WorkBill bill = billDao.selectOne(billId);
		if ("1".equals(bill.getDealStatus())) {
			throw new BadRequestException("调拨单已确认，不可重复确认！");
		}

		List<WorkBillDetail> details = detailDao.selectByBill(billId);
		Integer from = bill.getFromRegion();
		Integer to = bill.getToRegion();
		for (WorkBillDetail detail : details) {
			if (from != null) {
				goodsDao.reduceStock(detail.getGoodsId(), from, detail.getQuantity().intValue());
			}
			if (to != null) {
				goodsDao.addStock(detail.getGoodsId(), to, detail.getQuantity().intValue());
			}
		}

		bill.setStatus("1");
		bill.setDealTime(new Date());
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
