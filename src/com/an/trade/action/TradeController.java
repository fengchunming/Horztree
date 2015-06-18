package com.an.trade.action;

import com.an.base.dao.RegionDao;
import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.core.exception.ServerErrorException;
import com.an.mm.dao.GoodsDao;
import com.an.mm.entity.Goods;
import com.an.mm.entity.WorkBill;
import com.an.mm.entity.WorkBillDetail;
import com.an.trade.dao.TradeBillDetailDao;
import com.an.trade.dao.TradeDao;
import com.an.trade.entity.Trade;
import com.an.trade.entity.TradeBillDetail;
import com.an.utils.HttpInvoker;
import com.an.utils.Util;
import com.an.mm.dao.WorkBillDao;
import com.an.mm.dao.WorkBillDetailDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 订单管理
 *
 * @author Karas
 * @date 2012-3-8
 */
@Controller
@RequestMapping("/trade")
public class TradeController {

    private static final Logger logger = LoggerFactory.getLogger(TradeController.class);

    @Autowired
    private TradeDao tradeDao;

    @Autowired
    private TradeBillDetailDao tradeBillDetailDao;

    @Autowired
    private GoodsDao goodsDao;
    
	@Autowired
	private WorkBillDao billDao;

	@Autowired
	private WorkBillDetailDao detailDao;
	
	@Autowired
	private RegionDao regionDao;

    /**
     * 查询订单列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/bill/list", method = RequestMethod.GET)
    public Map<?, ?> selectTrades(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        String regionId = mParam.get("regionId").toString();
        String regionCode = regionDao.selectOne(Integer.valueOf(regionId)).getCode();
        mParam.put("regionCode", regionCode);
        mParam.remove("regionId");
        result.put("list", tradeDao.selectList(mParam));
        result.put("count", tradeDao.count(mParam));
        return result;
    }
    
    /**
     * 查询待发货订单数量
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/bill/waitSend", method = RequestMethod.GET)
    public Map<?, ?> selectWaitSendTrades(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        mParam.put("own", true);
        mParam.put("status", "1");//待发货
        Map<String, Object> result = new HashMap<>();
        //result.put("list", tradeDao.selectList(mParam));
        result.put("count", tradeDao.count(mParam));
        return result;
    }

    /**
     * 查询订单详细
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/bill/{id}", method = RequestMethod.GET)
    public Trade selectTrade(@PathVariable("id") int id) throws BadRequestException {
        return tradeDao.selectOne(id);
    }

    /**
     * 保存单据（新增）
     *
     * @param trade
     * @return
     * @throws BadRequestException
     * @throws ServerErrorException
     */
    @RequestMapping(value = "/bill", method = RequestMethod.POST)
    @Transactional
    public Trade insertTrade(@RequestBody Trade trade) throws BadRequestException, ServerErrorException {
        if (tradeDao.insert(trade) != 1) {
            throw new ServerErrorException("保存失败！");
        } else {
            saveTradeDetail(trade);
            return trade;
        }
    }

    /**
     * 保存单据（修改）
     * @param trade
     * @return
     * @throws BadRequestException
     * @throws ServerErrorException
     */
    @RequestMapping(value = "/bill/{id}", method = RequestMethod.PUT)
    @Transactional
    public Trade updateTrade(@RequestBody Trade trade) throws BadRequestException, ServerErrorException {
        if ("4".equals(trade.getDealStatus())) {
            throw new BadRequestException("已完结订单不可变更!");
        }
        if (tradeDao.update(trade) != 1) {
            throw new ServerErrorException("保存失败！");
        } else {
            saveTradeDetail(trade);
            return trade;
        }
    }

    /**
     * 取消订单
     *
     * @param id
     * @throws BadRequestException
     */
    @Transactional
    @RequestMapping(value = "/bill/{id}", method = RequestMethod.DELETE)
    public void deleteTrade(@PathVariable("id") int id) throws BadRequestException {
    	Trade trade = tradeDao.selectOne(id);
        if ("345".indexOf(trade.getStatus())!=-1) {
            throw new BadRequestException("已发货，已完结，已取消的订单不可取消!");
        }
        
        if ("WX".equals(trade.getPayment())) {//微信支付，退款
        	try {
				String returnMsg = HttpInvoker.readContentFromGet(trade.getBillCode());
				if (!"success".equals(returnMsg)) {
					throw new BadRequestException("微信支付退款失败！");
				}
			} catch (IOException e) {
				throw new BadRequestException("微信支付退款失败！", e);
			}
        }
        
        Integer regionId = trade.getRegionId();
        List<TradeBillDetail> details = tradeBillDetailDao.selectByBill(id);
        for (TradeBillDetail detail : details) {
        	Goods goods = goodsDao.selectGoodsInventory(detail.getGoodsId(), regionId);
 		    if (goods == null) {
 		    	throw new BadRequestException("仓库【" + regionDao.selectOne(regionId).getFullName() + "】中的商品【" + detail.getGoodsName() + "】不存在，无法完成锁定库存的释放！");
 		    }
 		    goods.setStockSum(goods.getStockSum() + detail.getQuantity().intValue());
 		    goods.setStockLocked(goods.getStockLocked() - detail.getQuantity().intValue());
 		    goodsDao.updateGoodsInventory(goods);
        }
        
        if (tradeDao.delete(id) <= 0) {
            throw new BadRequestException("删除失败");
        } else {
            tradeBillDetailDao.deleteByBill(id);
        }
    }

    /**
     * 查看订单明细列表
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    public List<TradeBillDetail> selectTradeDetail(@PathVariable("id") int id) throws BadRequestException {
        return tradeBillDetailDao.selectByBill(id);
    }

    /**
     * 保存明细列表（包括，新增，修改，删除）
     *
     * @param trade
     * @throws BadRequestException
     */
    private void saveTradeDetail(Trade trade) throws BadRequestException {
        if (trade.getItems() == null) return;
        for (TradeBillDetail item : trade.getItems()) {
            if (item.getId() == null && "d".equals(item.getStatus()))
                continue;
            item.setBillId(trade.getId());
            tradeBillDetailDao.save(item);
        }
    }

    /**
     * 订单发货处理
     * @param ids
     * @throws BadRequestException
     */
    @RequestMapping(value = "/check", method = RequestMethod.GET)
    @Transactional
    public void checkTrade(@RequestParam("ids[]") Integer[] ids) throws BadRequestException {
        for (int id : ids) {
           	Trade trade = tradeDao.selectOne(id);
            if ("345".indexOf(trade.getStatus())!=-1) {
                throw new BadRequestException("已发货，已完结，已取消的订单不可发货!");
            }
            
            trade.setStatus("3");
            tradeDao.updateState(trade);
            
            Integer regionId = trade.getRegionId();
            List<TradeBillDetail> details = tradeBillDetailDao.selectByBill(id);
            
            //生产出货单
            WorkBill wb = new WorkBill();
            wb.setBillName("发货单【" + trade.getShipRegion() + "-" + trade.getBillDate() + "-" + trade.getBillCode() + "】");
            wb.setBillType("DG");//发货单
            wb.setFromRegion(regionId);

            for (TradeBillDetail detail : details) {
            	WorkBillDetail wbd = new WorkBillDetail();
            	wbd.setBillId(1);
            	wbd.setCostPrice(new BigDecimal(detail.getSalePrice()));
            	wbd.setGoodsId(detail.getGoodsId());
            	wbd.setGoodsCode(null);
            	wbd.setGoodsBarcode(detail.getBarcode());
            	wbd.setGoodsName(detail.getGoodsName());
            	wbd.setQuantity(new BigDecimal(detail.getQuantity()));
            	wbd.setUomId(detail.getUom());
            	wbd.setSubTotal(new BigDecimal(detail.getSubTotal()));
     		    wb.addDetail(wbd);
            }
            
            billDao.save(wb);
            for (WorkBillDetail detail : wb.getDetails()) {
				detail.setBillId(wb.getId());
				detailDao.save(detail);
			}
            
    		Integer from = wb.getFromRegion();
    		for (WorkBillDetail detail : wb.getDetails()) {
    			if (from != null) {
    				goodsDao.reduceStock(detail.getGoodsId(), from, detail.getQuantity().intValue(), true);
    			}
    		}

    		wb.setDealStatus("1");
    		wb.setDealTime(new Date());
    		billDao.updateDealStatus(wb);
        }
    }

    /**
     * 订单打印
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/print/{id}", method = RequestMethod.GET)
    public ModelAndView printTrade(@PathVariable("id") int id) throws BadRequestException {
        ModelAndView mav = new ModelAndView("report/trade");
        Trade trade = tradeDao.selectOne(id);
        
        List<TradeBillDetail> details = tradeBillDetailDao.selectByBill(id);
        int count = tradeBillDetailDao.countByBill(id);
        //2015-06-01,六一活动，满6元送1包奶（仅限不夜城下单用户）
//        if (trade.getAmount().compareTo(new BigDecimal(6.00)) >= 0 && "我爱不夜城".equals(trade.getShipName())) {
//       	TradeBillDetail detail = new TradeBillDetail();
//        	detail.setPn("");//pn商品编号
//        	detail.setName("牛奶一盒（赠品）");//商品名称
//        	detail.setSalePrice(new BigDecimal(0.00));//单价
//        	detail.setPlanQuantity(new BigDecimal(1.00));//数量
//        	MaterialUom uom = new MaterialUom();
//        	uom.setId(9);//盒
//        	detail.setUom(uom);//单位
//        	detail.setSaleTotal(new BigDecimal(0.00));//小计
//        	details.add(detail);
//        	count++;
//        }
        mav.addObject("bill", trade);
        mav.addObject("list", details);
        mav.addObject("count", count);
        return mav;
    }

    /**
     * 销售分析报告
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public Map<?, ?> saleReport(WebRequest request) throws BadRequestException {
        Map<String, Object> params = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", tradeDao.report(params));
        result.put("count", "");
        return result;
    }

    /**
     * 异常
     * @param e
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequestException(Exception e) {
        logger.error(e.getMessage());
        return new ErrorModelAndView(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView otherException(Exception e) {
        logger.warn(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
