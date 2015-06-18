package com.an.base.action;

import com.an.base.dao.UomDao;
import com.an.base.entity.Uom;
import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
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
 * 度量单位管理
 *
 * @author Karas 2012-3-8
 */
@Controller
@RequestMapping("/base")
public class UomController {

	private static final Logger logger = LoggerFactory.getLogger(UomController.class);

	@Autowired
	private UomDao uomDao;

	@RequestMapping(value = "/uom/list", method = RequestMethod.GET)
	public Map<?, ?> query(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", uomDao.selectList(mParam));
		result.put("count", uomDao.count(mParam));
		return result;
	}

	@RequestMapping(value = "/uom/{id}", method = RequestMethod.GET)
	public Uom load(@PathVariable("id") int id) throws BadRequestException {
		return uomDao.selectOne(id);
	}

	@RequestMapping(value = "/uom", method = RequestMethod.POST)
	public Uom insert(@RequestBody Uom uom) throws BadRequestException {
		if (uomDao.insert(uom) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return uom;
		}
	}

	@RequestMapping(value = "/uom/{id}", method = RequestMethod.PUT)
	public Uom update(@RequestBody Uom uom) throws BadRequestException {
		if (uomDao.update(uom) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return uom;
		}
	}

	@RequestMapping(value = "/uom/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") int id) throws BadRequestException {
		if (uomDao.delete(id) <= 0)
			throw new BadRequestException("删除失败");
	}

	@RequestMapping(value = "/kv/uom", method = RequestMethod.GET)
	public List<Map<String, Object>> selectUom() throws BadRequestException {
		return uomDao.selectKV();
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
