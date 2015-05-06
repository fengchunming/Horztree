package com.an.pos.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.pos.dao.TerminalDao;
import com.an.pos.entity.Terminal;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台系统功能模块管理
 *
 * @author Karas
 * @date 2012-3-8
 */
@Controller
@RequestMapping("/pos")
public class TerminalController {

    private static final Logger logger = LoggerFactory
            .getLogger(TerminalController.class);

    @Autowired
    private TerminalDao terminalDao;

    /**
     * 查询模块列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/terminalList", method = RequestMethod.GET)
    public Map<?, ?> selectTerminals(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<String, Object>();
        Collection<Terminal> terminals = terminalDao.selectList(mParam);
        result.put("list", terminals);
        result.put("count", terminalDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/terminal/{id}", method = RequestMethod.GET)
    public Terminal selectTerminal(@PathVariable(value="id") int id)
            throws BadRequestException {
        return terminalDao.selectOne(id);
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/loadTerminalKV.do", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> selectTerminalKV()
            throws BadRequestException {
        return terminalDao.selectKV();
    }

    /**
     * 更新功能
     *
     * @param terminal
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/terminal/{id}", method = RequestMethod.POST)
    public Terminal saveTerminal(@RequestBody Terminal terminal)
            throws BadRequestException {
        if (terminalDao.save(terminal) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return terminal;
        }
    }

    /**
     * 删除功能
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/terminal/{id}", method = RequestMethod.DELETE)
    public void deleteTerminal(@PathVariable(value="id") int id) throws BadRequestException {
        if (terminalDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
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
