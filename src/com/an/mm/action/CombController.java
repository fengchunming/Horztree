package com.an.mm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.GoodsCombDao;
import com.an.utils.Util;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品货品关联关系管理
 *
 * @author Karas 2012-03-08
 */

@Controller
@RequestMapping("/mm")
public class CombController {

	private static final Logger logger = LoggerFactory.getLogger(CombController.class);

	@Autowired
	private GoodsCombDao goodsCombDao;

	/**
	 * 查询商品列表
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/comb", method = RequestMethod.GET)
	public Map<?, ?> queryComb(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", goodsCombDao.selectList(mParam));
		result.put("count", goodsCombDao.count(mParam));
		return result;
	}

	/**
	 * 查询商品关联的所有货品
	 * @param goodsId
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/items/{goodsId}", method = RequestMethod.GET)
	public List<Item> query(@PathVariable("goodsId") int goodsId) throws BadRequestException {
		return goodsCombDao.selectItemsByGoodsId(goodsId);
	}

	/**
	 * 保存商品关联的货品
	 * @param goodsId
	 * @param items
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/comb/{goodsId}", method = RequestMethod.POST)
	@Transactional
	public void saveComb(@PathVariable("goodsId") int goodsId, @RequestBody List<Item> items) throws BadRequestException {
		goodsCombDao.delete(goodsId);
		for (Item item : items) {
			item.setGoodsId(goodsId);
			goodsCombDao.insertItem(item);
		}
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
		logger.warn(e.getMessage(), e);
		return new ErrorModelAndView(e);
	}

}
