package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.OptionDao;
import com.an.sys.entity.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * 后台系统功能模块管理
 *
 * @author Karas
 * @date 2012-3-8
 */
@Controller
@RequestMapping("/sys")
public class OptionController {

    private static final Logger logger = LoggerFactory
            .getLogger(OptionController.class);

    @Autowired
    private OptionDao optionDao;


    /**
     * 通过主键ID查询功能详情
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/setting", method = RequestMethod.GET)
    public Setting loadSetting()
            throws BadRequestException {
        Map rst = optionDao.selectOne("option");
        Setting setting = new Setting();
        if (rst != null) {
            setting = (Setting) rst.get("option");
        }
        return setting;
    }

    /**
     * 更新功能
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/setting", method = RequestMethod.POST)
    public Setting saveSetting(@RequestBody Setting setting) throws BadRequestException {
        Map m = new HashMap<>();
        m.put("name", "option");
        m.put("option", setting);
        int rst = 0;
        if (optionDao.selectOne("option") == null) {
            rst = optionDao.insert(m);
        } else {
            rst = optionDao.update(m);
        }
        if (rst != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return setting;
        }
    }

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
        logger.warn(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
