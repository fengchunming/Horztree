package com.an.mm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.GoodsDao;
import com.an.mm.entity.Goods;
import com.an.mm.entity.Picture;
import com.an.sys.entity.Setting;
import com.an.utils.ImageUtil;
import com.an.utils.Upload;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台系统-商品管理
 *
 * @author Karas   2012-03-08
 */

@Controller
@RequestMapping("/mm")
public class GoodsController {

    private static final Logger logger = LoggerFactory.getLogger(GoodsController.class);

    @Autowired
    private GoodsDao goodsDao;

    /**
     * 查询商品列表
     */
    @RequestMapping(value = "/goods/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", goodsDao.selectList(mParam));
        result.put("count", goodsDao.count(mParam));
        return result;

    }

    /**
     * 通过主键ID查询商品详情
     */
    @RequestMapping(value = "/goods/{id}", method = RequestMethod.GET)
    public Goods load(@PathVariable("id") int id)
            throws BadRequestException {
        return goodsDao.selectOne(id);
    }

    /**
     * 新增商品-保存
     */
    @RequestMapping(value = "/goods", method = RequestMethod.POST)
    public Goods insert(@RequestBody Goods goods) throws BadRequestException {
        if (goodsDao.insert(goods) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            goodsDao.saveCategory(goods);
            return goods;
        }
    }

    /**
     * 修改商品-保存
     */
    @RequestMapping(value = "/goods/{id}", method = RequestMethod.PUT)
    public Goods update(@RequestBody Goods goods) throws BadRequestException {
        if (goodsDao.update(goods) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            goodsDao.saveCategory(goods);
            return goods;
        }
    }

    @RequestMapping(value = "/goods/upload", method = RequestMethod.POST)
    public Picture update(HttpServletRequest request) throws BadRequestException {
        try {
            String filename = Upload.htmlUpload(request, Setting.resourcePath);
            String source = Setting.resourcePath + filename;
            String s = source.substring(0, source.lastIndexOf("/") + 1) + "s_" + source.substring(source.lastIndexOf("/") + 1);
            String xs = source.substring(0, source.lastIndexOf("/") + 1) + "xs_" + source.substring(source.lastIndexOf("/") + 1);

            ImageUtil.fill(source, s, 220, 220);
            ImageUtil.fill(source, xs, 90, 90);
            return new Picture(filename);
        } catch (Exception e) {
            throw new BadRequestException(e);
        }
    }

    /**
     * 删除商品分类
     */
    @RequestMapping(value = "/goods/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id)
            throws BadRequestException {
        if (goodsDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }

    /**
     * 通过主键ID查询商品分类详情
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/kv/goods", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> loadKV() throws BadRequestException {
        return goodsDao.selectKV();
    }

    /**
     * 查询商品已发布的所有网点
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/goodsGroup", method = RequestMethod.GET)
    public List<Map<String, Object>> goodsGroup(@RequestParam("goods") int id) throws BadRequestException {
        return goodsDao.selectGoodsGroup(id);
    }

    /**
     * 查询网点发布的所有商品
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/groupGoods", method = RequestMethod.GET)
    public List<Map<String, Object>> groupGoods(@RequestParam("group") int id) throws BadRequestException {
        return goodsDao.selectGroupGoods(id);
    }

    /**
     * 商品发布保存（商品发布的所有网点）
     * @param id
     * @param ids
     * @throws BadRequestException
     */
    @RequestMapping(value = "/goodsGroup/{id}", method = RequestMethod.POST)
    public void linkGroups(@PathVariable("id") int id, @RequestBody Map[] ids) throws BadRequestException {
        Map<String, Object> param = new HashMap<>();
        param.put("goods", id);
        param.put("groups", ids);
        param.put("type", "group");
        goodsDao.saveGroupGoods(param);
    }

    /**
     * 商品发布保存（网点发布的所有商品）
     * @param id
     * @param ids
     * @throws BadRequestException
     */
    @RequestMapping(value = "/groupGoods/{id}", method = RequestMethod.POST)
    public void linkGoods(@PathVariable("id") int id, @RequestBody Map[] ids) throws BadRequestException {
        Map<String, Object> param = new HashMap<>();
        param.put("goods", ids);
        param.put("group", id);
        param.put("type", "goods");
        goodsDao.saveGroupGoods(param);
    }

    /**
     * 所有可售（关联所有网点）
     * @param id
     * @throws BadRequestException
     */
    @RequestMapping(value = "/goodsGroup/all", method = RequestMethod.POST)
    public void allGoods(@RequestParam("goods") int id) throws BadRequestException {
        goodsDao.linkAllGroup(id);
    }

    /**
     * 所有可售（关联所有商品）
     * @param id
     * @throws BadRequestException
     */
    @RequestMapping(value = "/groupGoods/all", method = RequestMethod.POST)
    public void allGroup(@RequestParam("group") int id) throws BadRequestException {
    	goodsDao.linkAllGoods(id);
    }

    /**
     * 查询商品分类列表
     */
    @RequestMapping(value = "/goods/autocomplete/{orgCode}", method = RequestMethod.GET)
    public Collection<Goods> autocomplete(
            @PathVariable("orgCode") String code, @RequestParam String q,
            @RequestParam String limit) throws BadRequestException {
        Map<String, Object> mParam = new HashMap<>();
        if (code != null && !code.equals("")) {
            mParam.put("supplier", code);
        }
        mParam.put("_LM", limit);
        return goodsDao.selectList(mParam);
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
