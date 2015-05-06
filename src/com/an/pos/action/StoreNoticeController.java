package com.an.pos.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.pos.dao.StoreNoticeDao;
import com.an.pos.entity.StoreNotice;
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
public class StoreNoticeController {

    private static final Logger logger = LoggerFactory
            .getLogger(StoreNoticeController.class);

    @Autowired
    private StoreNoticeDao storeNoticeDao;

    /**
     * 查询模块列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/storeNoticeList", method = RequestMethod.GET)
    public Map<?, ?> selectStoreNotices(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<String, Object>();
        Collection<StoreNotice> storeNotices = storeNoticeDao.selectList(mParam);
        result.put("list", storeNotices);
        result.put("count", storeNoticeDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/storeNotice/{id}", method = RequestMethod.GET)
    public StoreNotice selectStoreNotice(@PathVariable(value="id") int id)
            throws BadRequestException {
        StoreNotice storeNotice = storeNoticeDao.selectOne(id);
        return storeNotice;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/loadStoreNoticeKV.do", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> selectStoreNoticeKV() throws BadRequestException {
        return storeNoticeDao.selectKV();
    }


    /**
     * 更新功能
     *
     * @param storeNotice
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/storeNotice/{id}", method = RequestMethod.POST)
    public StoreNotice saveStoreNotice(@RequestBody StoreNotice storeNotice) throws BadRequestException {
        if (storeNoticeDao.save(storeNotice) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return storeNotice;
        }
    }

    /**
     * 删除功能
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/storeNotice/{id}", method = RequestMethod.DELETE)
    public void deleteStoreNotice(@PathVariable(value="id") int id)
            throws BadRequestException {
        if (storeNoticeDao.delete(id) <= 0)
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
