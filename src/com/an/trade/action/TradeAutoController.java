package com.an.trade.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.trade.dao.TradeAutoDao;
import com.an.trade.entity.TradeConfig;
import com.an.utils.FileUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
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
     */
    @RequestMapping(value = "/billAuto", method = RequestMethod.POST)
    public  Map<String,Object> autoBillPro(@RequestParam String billDate,@RequestParam int maxAmount) {
    	Map<String, Object> mParam = new HashMap<>();
    	mParam.put("billDate", billDate);
    	mParam.put("maxAmount", maxAmount);
        return tradeAutoDao.autoBillPro(mParam);
    }
    
    @SuppressWarnings("unchecked")
	@RequestMapping(value = "/batchBillAuto", method = RequestMethod.POST)
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
