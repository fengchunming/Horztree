package com.an.mm.action;

import com.an.mm.entity.Category;
import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.CategoryDao;
import com.an.utils.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品分类管理
 *
 * @author Karas 2012-3-8
 */
@Controller
@RequestMapping("/mm")
public class CategoryController {

	private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

	@Autowired
	private CategoryDao categoryDao;

	/**
	 * 查询商品分类列表
	 * @param request
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/category/list", method = RequestMethod.GET)
	public Map<?, ?> query(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", categoryDao.selectList(mParam));
		result.put("count", categoryDao.count(mParam));
		return result;
	}

	/**
	 * 查询商品分类详情
	 * @param id
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/category/{id}", method = RequestMethod.GET)
	public Category load(@PathVariable("id") int id) throws BadRequestException {
		return categoryDao.selectOne(id);
	}

	/**
	 * 新增商品分类
	 * @param category
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/category", method = RequestMethod.POST)
	public Category insert(@RequestBody Category category) throws BadRequestException {
		if (categoryDao.save(category) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return category;
		}
	}

	/**
	 * 更新商品分类
	 * @param category
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/category/{id}", method = RequestMethod.PUT)
	public Category saveCategory(@RequestBody Category category) throws BadRequestException {
		if (categoryDao.save(category) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return category;
		}
	}

	/**
	 * 删除商品分类
	 * @param id
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/category/{id}", method = RequestMethod.DELETE)
	public void deleteCategory(@PathVariable("id") int id) throws BadRequestException {
		if (categoryDao.delete(id) <= 0)
			throw new BadRequestException("删除失败");
	}

	/**
	 * 商品分类数据字典
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/kv/category", method = RequestMethod.GET)
	public List<Map<String, Object>> selectCategory() throws BadRequestException {
		return categoryDao.selectKV(new HashMap<String, String>());
	}

	/**
	 * 叶子节点分类KV值
	 * @return
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/kv/categoryLeaf", method = RequestMethod.GET)
	public List<Map<String, Object>> loadSmallCategoryKV() throws BadRequestException {
		Map<String, String> param = new HashMap<>();
		param.put("isLeaf", "t");
		return categoryDao.selectKV(param);
	}

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
