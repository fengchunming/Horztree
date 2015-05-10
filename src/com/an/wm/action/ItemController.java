package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.utils.Util;
import com.an.wm.dao.ItemDao;
import com.an.wm.entity.Item;
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
 * 后台系统货品档案管理
 *
 * @author Karas   2012-03-08
 */

@Controller
@RequestMapping("/wm")
public class ItemController {

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    private ItemDao itemDao;

    @RequestMapping(value = "/item/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", itemDao.selectList(mParam));
        result.put("count", itemDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.GET)
    public Item load(@PathVariable("id") int id)
            throws BadRequestException {
        return itemDao.selectOne(id);
    }

    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public Item insert(@RequestBody Item item) throws BadRequestException {
        if (itemDao.insert(item) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return item;
        }
    }

    @RequestMapping(value = "/item/{id}", method = RequestMethod.PUT)
    public Item update(@RequestBody Item item) throws BadRequestException {
        if (itemDao.update(item) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return item;
        }
    }


    @RequestMapping(value = "/item/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id)
            throws BadRequestException {
        if (itemDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }


    @RequestMapping(value = "/kv/item", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> loadKV()
            throws BadRequestException {
        return itemDao.selectKV();
    }


    /**
     * 查询商品分类列表
     */
    @RequestMapping(value = "/goods/autocomplete/{orgCode}", method = RequestMethod.GET)
    public Collection<Item> autocomplete(
            @PathVariable("orgCode") String code, @RequestParam String q,
            @RequestParam String limit) throws BadRequestException {
        Map<String, Object> mParam = new HashMap<String, Object>();
        if (code != null && !code.equals("")) {
            mParam.put("supplier", code);
        }
        mParam.put("_LM", limit);
        return itemDao.selectList(mParam);
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
