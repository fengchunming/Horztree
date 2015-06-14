package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.trade.dao.TradeBillDetailDao;
import com.an.wm.dao.ResupplyDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * 门店补货（调拨）
 *
 * @author karas
 */
@Controller
@RequestMapping("/wb")
public class ResupplyController {

    private static final Logger logger = LoggerFactory.getLogger(ResupplyController.class);

    @Autowired
    private ResupplyDao resupplyDao;

    @Autowired
    private TradeBillDetailDao tradeBillDetailDao;

    @Autowired
    private IssueController issueService;

    /**
     * 异常处理
     *
     * @param e
     * @return
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
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
