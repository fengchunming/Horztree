package com.an.trade.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.OrganizationDao;
import com.an.trade.dao.TradeDao;
import com.an.trade.dao.TradeDetailDao;
import com.an.trade.entity.Trade;
import com.an.wm.action.IssueController;
import com.an.wm.entity.WorkBill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 交易管理(业务单据管理)
 *
 * @author Karas
 * @date 2012-3-8
 */
@Controller
@RequestMapping("/trade")
public class SellController {

    private static final Logger logger = LoggerFactory
            .getLogger(SellController.class);

    @Autowired
    private TradeDao tradeDao;

    @Autowired
    private TradeDetailDao tradeDetailDao;

    @Autowired
    private OrganizationDao orgDao;

    @Autowired
    IssueController issueService;


    /**
     * 审核单据
     *
     * @param ids
     * @throws BadRequestException
     */
    @RequestMapping(value = "/checkTrade/{ids}", method = RequestMethod.GET)
    public void checkTrade(@PathVariable(value="ids") String ids) throws BadRequestException {
        Map<String, Object> param = new HashMap<>();
        param.put("billCodes", ids.split("-"));
        Collection<Trade> trades = tradeDao.selectList(param);
        for (Trade trade : trades) {
            if (trade.getStatus().equals("check"))
                continue;
            issueService.makeIssue(trade, false);
            trade.setStatus("check");
            tradeDao.updateState(trade);
        }
    }

    /**
     * 暂停单据执行
     *
     * @param id
     * @throws BadRequestException
     */
    @RequestMapping(value = "/pauseTrade/{id}", method = RequestMethod.GET)
    public void pauseTrade(@PathVariable(value="id") String id) throws BadRequestException {
        Trade trade = tradeDao.selectByCode(id);
        trade.setStatus("pause");
        tradeDao.updateState(trade);
    }

    /**
     * 停止并拦截单据
     *
     * @param id
     * @throws BadRequestException
     */
    @RequestMapping(value = "/stopTrade/{id}", method = RequestMethod.GET)
    public void stopTrade(@PathVariable(value="id") String id) throws BadRequestException {
        Trade trade = tradeDao.selectByCode(id);
        trade.setStatus("stop");
        tradeDao.updateState(trade);
    }

    /**
     * 生成发货单并发货
     *
     * @param trade
     * @throws BadRequestException
     */
    @RequestMapping(value = "/tradeIssue", method = RequestMethod.POST)
    public void tradeIssue(@RequestBody Trade trade) throws BadRequestException {
        trade.setDealStatus("WAIT_BUYER_CONFIRM_GOODS");
        trade.setStatus("solved");
        tradeDao.updateState(trade);
        WorkBill issue = issueService.makeIssue(trade, true);
        if (issue != null) {
//            issueService.checkIssue(issue.getId(), 0);
        }

		/*
         * 第三方平台发货
		 */
//        Organization org = orgDao.selectOne(trade.getPartyB().getId());
//        ApiService.shipSend(trade, org);
    }

//    @RequestMapping(value = "/tradeSync", method = RequestMethod.GET)
//    public void tradeSync() throws BadRequestException {
//        if (ApiService.stores.isEmpty()) {
//            throw new BadRequestException("请先进行网店授权");
//        }
//        Iterator<Entry<String, Organization>> iter = ApiService.stores
//                .entrySet().iterator();
//        while (iter.hasNext()) {
//            try {
//                Entry<String, Organization> entry = iter.next();
//                Organization store = entry.getValue();
//                if (!"taobao".equals(store.getType())) {
//                    continue;
//                }
//                if (store.getToken() == null || store.getToken().isEmpty())
//                    continue;
//                Date now = new Date();
//                store.setSyncStamp(Util.StampFmt.format(new Date(
//                        now.getTime() - 86400000)));
//                topSync.tradesSoldGet(store);
//                store.setSyncStamp(Util.StampFmt.format(now));
//                orgDao.updateSync(store);
//            } catch (Exception e) {
//                logger.error(e.getMessage(), e);
//            }
//        }
//    }

    /**
     * 异常
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
