package com.an.crm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.crm.dao.CommentDao;
import com.an.crm.entity.Comment;
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
import java.util.Map;

@Controller
@RequestMapping("/crm")
public class CommentController {

	private static final Logger logger = LoggerFactory.getLogger(CommentController.class);

	@Autowired
	private CommentDao commentDao;

	@RequestMapping(value = "/comment/list", method = RequestMethod.GET)
	public Map<?, ?> query(WebRequest request) throws BadRequestException {
		Map<String, Object> mParam = Util.GetRequestMap(request);
		Map<String, Object> result = new HashMap<>();
		result.put("list", commentDao.selectList(mParam));
		result.put("count", commentDao.count(mParam));
		return result;
	}

	@RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
	public Comment load(@PathVariable("id") Integer id) throws BadRequestException {
		return commentDao.selectOne(id);
	}

	@RequestMapping(value = "/comment", method = RequestMethod.POST)
	public Comment insert(@RequestBody Comment comment) throws BadRequestException {
		if (commentDao.insert(comment) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return comment;
		}
	}

	@RequestMapping(value = "/comment/{id}", method = RequestMethod.PUT)
	public Comment update(@RequestBody Comment comment) throws BadRequestException {
		if (commentDao.update(comment) != 1) {
			throw new BadRequestException("保存失败！");
		} else {
			return comment;
		}
	}

	/**
	 * 回复
	 */
	@RequestMapping(value = "/reply/{id}", method = RequestMethod.POST)
	public void publish(@PathVariable("id") Integer id) throws BadRequestException {
		// TODO： 回复评价
		// if (commentDao.publish(id) != 1) {
		// throw new BadRequestException("保存失败！");
		// }

	}

	@RequestMapping(value = "/comment/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable("id") int id) throws BadRequestException {
		if (commentDao.delete(id) <= 0)
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
