package com.an.cms.action;

import com.an.cms.dao.NoticeDao;
import com.an.cms.entity.Notice;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/cms")
public class NoticeController {

	private static final Logger logger = LoggerFactory.getLogger(NoticeController.class);

	@Autowired
	private NoticeDao noticeDao;

	/**
	 * 查询公告列表
	 */
	@RequestMapping(value = "/notice/list", method = RequestMethod.GET)
	public Map<?, ?> query(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", noticeDao.selectList(mParam));
		result.put("count", noticeDao.count(mParam));
		return result;
	}

	/**
	 * 查询公告列表
	 */
	@RequestMapping(value = "/notice/week", method = RequestMethod.GET)
	public Map<?, ?> queryByWeek(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date now = new Date();
		mParam.put("status", "p");
		mParam.put("type", "1");
		mParam.put("startTime", sdf.format(new Date(now.getTime() - 7*24*60*60*1000)));
		mParam.put("endTime", sdf.format(now));
		Map<String, Object> result = new HashMap<>();
		result.put("list", noticeDao.selectList(mParam));
		result.put("count", noticeDao.count(mParam));
		return result;
	}

	/**
	 * 查询公告详情
	 */
	@RequestMapping(value = "/notice/{id}", method = RequestMethod.GET)
	public Notice load(@PathVariable("id") Integer id) throws BadRequestException {
		return noticeDao.selectInit(id);
	}

	/**
	 * 保存公告信息
	 */
	@RequestMapping(value = "/notice", method = RequestMethod.POST)
	public Notice insert(@RequestBody Notice notice) throws BadRequestException {
		if (noticeDao.insert(notice) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return notice;
		}
	}

	/**
	 * 更新公告信息
	 */
	@RequestMapping(value = "/notice/{id}", method = RequestMethod.PUT)
	public Notice update(@RequestBody Notice notice) throws BadRequestException {
		if (noticeDao.update(notice) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return notice;
		}
	}

	/**
	 * 发布公告信息
	 */
	@RequestMapping(value = "/noticePublish/{id}", method = RequestMethod.POST)
	public void publish(@PathVariable("id") Integer id) throws BadRequestException {
		if (noticeDao.publish(id) != 1) {
			throw new BadRequestException("保存失败！");
		}
	}

	/**
	 * 删除公告信息
	 */
	@RequestMapping(value = "/notice/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") int id) throws BadRequestException {
		if (noticeDao.delete(id) <= 0)
			throw new BadRequestException("删除失败");
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
