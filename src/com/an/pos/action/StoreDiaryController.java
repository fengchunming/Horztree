package com.an.pos.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.pos.dao.StoreDiaryDao;
import com.an.pos.entity.StoreDiary;
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
public class StoreDiaryController {

    private static final Logger logger = LoggerFactory
            .getLogger(StoreDiaryController.class);

    @Autowired
    private StoreDiaryDao storeDiaryDao;

    /**
     * 查询模块列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/storeDiaryList", method = RequestMethod.GET)
    public Map<?, ?> selectStoreDiarys(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<String, Object>();
        Collection<StoreDiary> storeDiarys = storeDiaryDao.selectList(mParam);
        result.put("list", storeDiarys);
        result.put("count", storeDiaryDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/storeDiary/{id}", method = RequestMethod.GET)
    public StoreDiary selectStoreDiary(@PathVariable(value="id") int id)
            throws BadRequestException {
        StoreDiary storeDiary = storeDiaryDao.selectOne(id);
        return storeDiary;
    }

    /**
     * 更新功能
     *
     * @param storeDiary
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/storeDiary/{id}", method = RequestMethod.POST)
    public StoreDiary saveStoreDiary(@RequestBody StoreDiary storeDiary) throws BadRequestException {
        if (storeDiaryDao.save(storeDiary) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return storeDiary;
        }
    }

    /**
     * 删除功能
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/storeDiary/{id}", method = RequestMethod.DELETE)
    public void deleteStoreDiary(@PathVariable(value="id") int id)
            throws BadRequestException {
        if (storeDiaryDao.delete(id) <= 0)
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
