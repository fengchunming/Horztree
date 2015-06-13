package com.an.trade.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.core.exception.ServerErrorException;
import com.an.mm.dao.GoodsCombDao;
import com.an.mm.dao.GoodsDao;
import com.an.mm.entity.Goods;
import com.an.sys.entity.Setting;
import com.an.trade.dao.TradeAutoDao;
import com.an.trade.dao.TradeDao;
import com.an.trade.dao.TradeDetailDao;
import com.an.trade.entity.TradeConfig;
import com.an.trade.entity.Trade;
import com.an.utils.FileUtil;
import com.an.utils.Util;
import com.an.wm.action.WorkBillController;
import com.an.wm.entity.Item;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 自动生成订单
 *
 * @author zjg
 * @date 2015-6-12
 */
@Controller
@RequestMapping("/trade")
public class TradeAutoController {

    private static final Logger logger = LoggerFactory.getLogger(TradeAutoController.class);

    @Autowired
    private TradeAutoDao tradeAutoDao;

  
    /**
     * 查询订单列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/billAuto", method = RequestMethod.POST)
    public  Map<String,Object> autoBillPro(@RequestParam String billDate,@RequestParam int maxAmount) throws BadRequestException {
        Map<String, Object> mParam = new HashMap<>();
    	mParam.put("billDate", billDate);
    	mParam.put("maxAmount", maxAmount);
        return tradeAutoDao.autoBillPro(mParam);
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/bashBillAuto", method = RequestMethod.POST)
    public  Map<String,Object> bashBillPro(HttpServletRequest request) throws Exception {
    	String realPath = request.getServletContext().getRealPath("");
    	//D:\IDE-JAVA\workspace\Horztree\web
    	//String path = "D:\\IDE-JAVA\\workspace\\Horztree\\src\\com\\an\\sys\\action";
        String configFilePath = realPath + File.separator + "template" + File.separator + "TradeConfig.xml";
		List<TradeConfig> list=FileUtil.importData(request, configFilePath);
		int leftAmountReturn=0;
		int existAmount=0;
    	for (TradeConfig obj : list) {
    		Map<String, Object> mParam = new HashMap<>();
        	mParam.put("billDate", obj.getBillDate());
        	mParam.put("maxAmount", obj.getMaxAmount());
        	Map<String, Object> result = tradeAutoDao.autoBillPro(mParam);
        	leftAmountReturn += Integer.parseInt(result.get("leftAmountReturn").toString());
        	existAmount += Integer.parseInt(result.get("existAmount").toString());;
		}
    	Map<String, Object> sum = new HashMap<String,Object>();
    	sum.put("leftAmountReturn", leftAmountReturn);
    	sum.put("existAmount", existAmount);
    	return sum;
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
