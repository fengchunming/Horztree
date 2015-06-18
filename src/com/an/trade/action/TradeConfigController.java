package com.an.trade.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.core.exception.ServerErrorException;
import com.an.trade.dao.TradeConfigDao;
import com.an.trade.entity.TradeConfig;
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

import java.util.HashMap;
import java.util.Map;


/**
 * 自动生成订单
 *
 * @author zjg
 * @date 2015-6-12
 */
@Controller
@RequestMapping("/trade")
public class TradeConfigController {

    private static final Logger logger = LoggerFactory.getLogger(TradeConfigController.class);

    @Autowired
    private TradeConfigDao tradeConfigDao;   
    

	/**
	 * 查看期望订单数量列表
	 * 
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/tradeConfig/list", method = RequestMethod.GET)
	public Map<?, ?> query(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", tradeConfigDao.selectList(mParam));
		result.put("count", tradeConfigDao.count(mParam));
		return result;
	}
	
	/**
	 * 查看期望订单数量列表
	 * 
	 * @param request
	 * @return
	 * @throws ServerErrorException 
	 */
	@RequestMapping(value = "/tradeConfig/{id}", method = RequestMethod.PUT)
	public TradeConfig modify(@RequestBody TradeConfig tradeConfig) throws  ServerErrorException {
		
		 if (tradeConfigDao.update(tradeConfig) != 1) {
	         throw new ServerErrorException("更新失败！");
	     } else{
		     return tradeConfig;
		 }
	}

   
    /**
     * 保存订单日期，期望订单数量
     *
     * @param request
     * @return
     */
	@RequestMapping(value = "/tradeConfig", method = RequestMethod.POST)
	public TradeConfig save(@RequestBody TradeConfig tradeConfig) {
		try{
			tradeConfigDao.insert(tradeConfig);
		}catch(Exception e){
			throw new BadRequestException("保存失败！日期不能重复");
		}
		return tradeConfig;
		/*if (tradeConfigDao.insert(tradeConfig) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return tradeConfig;
		}*/

	}
	  /**
     * 删除记录 彻底删除
     *
     * @param id
     * @throws BadRequestException
     */
    @Transactional
    @RequestMapping(value = "/tradeConfig/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) throws BadRequestException {
    	  if (tradeConfigDao.delete(id) <= 0) {
              throw new BadRequestException("删除失败");
          }
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
