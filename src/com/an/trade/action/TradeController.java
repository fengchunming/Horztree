package com.an.trade.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.core.exception.ServerErrorException;
import com.an.mm.dao.GoodsCombDao;
import com.an.mm.dao.GoodsDao;
import com.an.mm.entity.Goods;
import com.an.trade.dao.TradeDao;
import com.an.trade.dao.TradeDetailDao;
import com.an.trade.entity.Trade;
import com.an.utils.Util;
import com.an.wm.action.WorkBillController;
import com.an.wm.entity.Item;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 交易管理(业务单据管理)
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
    private TradeDetailDao tradeDetailDao;

    @Autowired
    private GoodsCombDao goodsCombDao;
    
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private WorkBillController billAction;

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
        result.put("list", tradeDao.selectList(mParam));
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
     * 根据单据号删除单条单据
     *
     * @param id
     * @throws BadRequestException
     */
    @Transactional
    @RequestMapping(value = "/bill/{id}", method = RequestMethod.DELETE)
    public void deleteTrade(@PathVariable(value = "id") int id) throws BadRequestException {
    	Trade trade = tradeDao.selectOne(id);
        if (trade.getStatus().equals("resoled")) {
            throw new BadRequestException("已完结订单不可变更!");
        }
        
        Integer groupId = trade.getGroupId();
        List<Item> details = tradeDetailDao.selectByBill(id);
        for (Item detail : details) {
        // TODO: goods -》 item
	        Map stock = goodsDao.selectStocksBy2Gids(groupId, detail.getGoodsId());
	        Integer newStockSum = (stock.get("stockSum") == null ? 0 : (Integer)stock.get("stockSum"));
	        Integer newStockLocked = (stock.get("stockLocked") == null ? 0 : (Integer)stock.get("stockLocked")) - detail.getPlanQuantity().intValue();
	        goodsDao.updateStocksBy2Gids(groupId, detail.getGoodsId(), newStockSum, newStockLocked);

        }
        
        if (tradeDao.delete(id) <= 0) {
            throw new BadRequestException("删除失败");
        } else {
            tradeDetailDao.deleteByBill(id);
        }
    }

    /**
     * 查看订单明细
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/details/{id}", method = RequestMethod.GET)
    public List<Item> selectTradeDetail(@PathVariable("id") int id) throws BadRequestException {
        return tradeDetailDao.selectByBill(id);
    }

    /**
     * 保存明细列表（包括，新增，修改，删除）
     *
     * @param trade
     * @throws BadRequestException
     */
    private void saveTradeDetail(Trade trade) throws BadRequestException {
        if (trade.getItems() == null) return;
        for (Item item : trade.getItems()) {
            if (item.getId() == null && "d".equals(item.getStatus()))
                continue;
            item.setBillId(trade.getId());
            tradeDetailDao.save(item);
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
            trade.setStatus("3");
            tradeDao.updateState(trade);
//            WorkBill bill = new WorkBill();
//            bill.setType("OT");
//            bill.setOriginBill(trade);
//            bill.setFrom(new Location(trade.getGroupId()));
//            Organization org = new Organization();
//            org.setAddr(trade.getAddr());
//            bill.setTarget(org);
//            // TODO 生成一个出库单
//
//            List<Item> details = tradeDetailDao.selectByBill(id);
//            for (Item detail : details) {
//                // TODO: goods -》 item
//                Map<String, Object> param = new HashMap<>();
//                param.put("goodsId", detail.getGoodsId());
//                param.put("pn", detail.getPn());
//                List<Item> items = goodsCombDao.selectDirectItems(param);
//                for (Item item : items) {
//                    System.out.println(item.getName());
////              bill.addItem(item);
//                }
//            }
            
            Integer groupId = trade.getGroupId();
            List<Item> details = tradeDetailDao.selectByBill(id);
            for (Item detail : details) {
            // TODO: goods -》 item
		        Map stock = goodsDao.selectStocksBy2Gids(groupId, detail.getGoodsId());
		        Integer newStockSum = (stock.get("stockSum") == null ? 0 : (Integer)stock.get("stockSum")) - detail.getPlanQuantity().intValue();
		        Integer newStockLocked = (stock.get("stockLocked") == null ? 0 : (Integer)stock.get("stockLocked")) - detail.getPlanQuantity().intValue();
		        goodsDao.updateStocksBy2Gids(groupId, detail.getGoodsId(), newStockSum, newStockLocked);

            }
            
        }

//        billAction.insertBill(bill);
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
        mav.addObject("bill", trade);
        mav.addObject("list", tradeDetailDao.selectByBill(id));
        mav.addObject("count", tradeDetailDao.countByBill(id));
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
