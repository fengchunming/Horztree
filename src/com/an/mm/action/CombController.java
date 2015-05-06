package com.an.mm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.GoodsCombDao;
import com.an.utils.Util;
import com.an.wm.entity.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台系统商品分类管理
 *
 * @author Karas   2012-03-08
 */

@Controller
@RequestMapping("/mm")
public class CombController {

    private static final Logger logger = LoggerFactory
            .getLogger(CombController.class);


    @Autowired
    private GoodsCombDao goodsCombDao;


    @RequestMapping(value = "/goods/comb", method = RequestMethod.GET)
    public Map<?, ?> queryComb(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", goodsCombDao.selectList(mParam));
        result.put("count", goodsCombDao.count(mParam));
        return result;

    }


    /**
     * 查询商品分类列表
     */
    @RequestMapping(value = "/goods/items", method = RequestMethod.GET)
    public List<Item> query(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        return goodsCombDao.selectItems(mParam);
    }

    @RequestMapping(value = "/goods/comb/{id}", method = RequestMethod.POST)
    @Transactional
    public void saveComb(@PathVariable(value = "id") int id, @RequestBody List<Item> items)
            throws BadRequestException {
        goodsCombDao.delete(id);
        for (Item item : items) {
            if (!"d".equals(item.getStatus())) {
                item.setGoodsId(id);
                goodsCombDao.insertItem(item);
            }
        }
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
