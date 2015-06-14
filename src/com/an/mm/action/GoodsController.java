package com.an.mm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.CategoryDao;
import com.an.mm.dao.GoodsDao;
import com.an.mm.entity.Goods;
import com.an.mm.entity.Picture;
import com.an.sys.entity.Setting;
import com.an.utils.ImageUtil;
import com.an.utils.Upload;
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

import javax.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * 商品管理
 *
 * @author Karas 2012-03-08
 */

@Controller
@RequestMapping("/mm")
public class GoodsController {

	private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

	@Autowired
	private GoodsDao goodsDao;
	
	@Autowired
	private CategoryDao categoryDao;

	/**
	 * 查询商品列表
	 * 
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/list", method = RequestMethod.GET)
	public Map<?, ?> query(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", goodsDao.selectList(mParam));
		result.put("count", goodsDao.count(mParam));
		return result;
	}

	/**
	 * 商品详情
	 * 
	 * @param id
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/{id}", method = RequestMethod.GET)
	public Goods load(@PathVariable("id") int id) throws BadRequestException {
		return goodsDao.selectOne(id);
	}

	/**
	 * 新增商品-保存
	 * 
	 * @param goods
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods", method = RequestMethod.POST)
	@Transactional
	public Goods insert(@RequestBody Goods goods) throws BadRequestException {
		if (goods.getCategoryId() != null && goods.getCategoryId() > 0) {
			String categoryCode = categoryDao.selectOne(goods.getCategoryId()).getCode();
			goods.setCategoryCode(categoryCode);
		}
		if (goodsDao.save(goods) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return goods;
		}
	}

	/**
	 * 修改商品-保存
	 * 
	 * @param goods
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/{id}", method = RequestMethod.PUT)
	@Transactional
	public Goods update(@RequestBody Goods goods) throws BadRequestException {
		if (goods.getCategoryId() != null && goods.getCategoryId() > 0) {
			String categoryCode = categoryDao.selectOne(goods.getCategoryId()).getCode();
			goods.setCategoryCode(categoryCode);
		}
		if (goodsDao.save(goods) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return goods;
		}
	}

	/**
	 * 商品图片上传
	 * 
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/upload", method = RequestMethod.POST)
	public Picture update(HttpServletRequest request) throws BadRequestException {
		try {
			String filename = Upload.htmlUpload(request, Setting.resourcePath);
			String source = Setting.resourcePath + filename;
			String s = source.substring(0, source.lastIndexOf("/") + 1) + "s_"
					+ source.substring(source.lastIndexOf("/") + 1);
			String xs = source.substring(0, source.lastIndexOf("/") + 1)
					+ "xs_" + source.substring(source.lastIndexOf("/") + 1);

			ImageUtil.fill(source, s, 220, 220);
			ImageUtil.fill(source, xs, 90, 90);
			return new Picture(filename);
		} catch (Exception e) {
			throw new BadRequestException(e);
		}
	}

	/**
	 * 删除商品
	 * 
	 * @param id
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/goods/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") int id) throws BadRequestException {
		if (goodsDao.delete(id) <= 0)
			throw new BadRequestException("删除失败");
	}

	/**
	 * 查询商品发布信息（库存等）
	 * 
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/inventory", method = RequestMethod.GET)
	public Map<?, ?> queryInventory(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", goodsDao.selectInventory(mParam));
		result.put("count", goodsDao.countInventory(mParam));
		return result;
	}

	/**
	 * 保存商品发布信息（库存等）
	 * @param goods
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/inventory/{id}", method = RequestMethod.PUT)
	@Transactional
	public Goods saveInventory(@RequestBody Goods goods) throws BadRequestException {
		if (goodsDao.updateGoodsInventory(goods) < 1) {//可能为2
			throw new BadRequestException("保存失败！");
		} else {
			return goods;
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
