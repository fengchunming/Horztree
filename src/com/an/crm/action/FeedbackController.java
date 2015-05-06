package com.an.crm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.crm.dao.FeedbackDao;
import com.an.crm.entity.Feedback;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/crm")
public class FeedbackController {

    private static final Logger logger = LoggerFactory
            .getLogger(FeedbackController.class);

    @Autowired
    private FeedbackDao feedbackDao;

    @RequestMapping(value = "/feedback/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", feedbackDao.selectList(mParam));
        result.put("count", feedbackDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/feedback/{id}", method = RequestMethod.GET)
    public Feedback load(@PathVariable(value="id") Integer id)
            throws BadRequestException {
        return feedbackDao.selectOne(id);
    }

    @RequestMapping(value = "/feedback", method = RequestMethod.POST)
    public Feedback insert(@RequestBody @Valid Feedback feedback, Errors rst)
            throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        }

        if (feedbackDao.insert(feedback) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return feedback;
        }
    }

    @RequestMapping(value = "/feedback/{id}", method = RequestMethod.PUT)
    public Feedback update(@RequestBody @Valid Feedback feedback, Errors rst) throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (feedbackDao.update(feedback) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return feedback;
        }
    }

    @RequestMapping(value = "/feedback/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id) throws BadRequestException {
        if (feedbackDao.delete(id) <= 0)
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
