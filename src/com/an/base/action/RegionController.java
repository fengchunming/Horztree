package com.an.base.action;

import com.an.base.dao.RegionDao;
import com.an.base.entity.Region;
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
 * 区域/地区管理
 *
 * @author Karas 2012-3-8
 */
@Controller
@RequestMapping("/base")
public class RegionController {

	private static final Logger logger = LoggerFactory.getLogger(RegionController.class);

	@Autowired
	private RegionDao regionDao;

	@RequestMapping(value = "/region/list", method = RequestMethod.GET)
	public Map<?, ?> query(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", regionDao.selectList(mParam));
		result.put("count", regionDao.count(mParam));
		return result;
	}

	@RequestMapping(value = "/region/{id}", method = RequestMethod.GET)
	public Region load(@PathVariable("id") int id) throws BadRequestException {
		return regionDao.selectOne(id);
	}

	@RequestMapping(value = "/region", method = RequestMethod.POST)
	public Region insert(@RequestBody Region region) throws BadRequestException {
		if (regionDao.save(region) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return region;
		}
	}

	@RequestMapping(value = "/region/{id}", method = RequestMethod.PUT)
	public Region update(@RequestBody Region region) throws BadRequestException {
		if (regionDao.save(region) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return region;
		}
	}

	@RequestMapping(value = "/region/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") int id) throws BadRequestException {
		if (regionDao.delete(id) <= 0)
			throw new BadRequestException("删除失败");
	}

	/**
	 * 夜猫店开始营业
	 * 
	 * @param id
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/region/work/{id}", method = RequestMethod.GET)
	public void work(@PathVariable("id") int id) throws BadRequestException {
		Region region = new Region();
		region.setId(id);
		region.setBusinessStatus("w");
		if (regionDao.updateBusinessStatus(region) <= 0)
			throw new BadRequestException("操作失败");
	}

	/**
	 * 夜猫店开始休息
	 * 
	 * @param id
	 * @throws BadRequestException
	 */
	@RequestMapping(value = "/region/sleep/{id}", method = RequestMethod.GET)
	public void sleep(@PathVariable("id") int id) throws BadRequestException {
		Region region = new Region();
		region.setId(id);
		region.setBusinessStatus("s");
		if (regionDao.updateBusinessStatus(region) <= 0)
			throw new BadRequestException("操作失败");
	}

	@RequestMapping(value = "/kv/region", method = RequestMethod.GET)
	public List<Map<String, Object>> loadKV() throws BadRequestException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type", "sm");
		return regionDao.selectKV(params);
	}
	
	@RequestMapping(value = "/kv/ownRegion", method = RequestMethod.GET)
	public List<Map<String, Object>> loadKVOwn() throws BadRequestException {
        Map<String, Object> params = new HashMap<>();
        params.put("own", true);
        params.put("type", "sm");
		return regionDao.selectKV(params);
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
