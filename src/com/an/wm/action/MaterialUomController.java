package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.wm.dao.MaterialUomDao;
import com.an.wm.entity.MaterialUom;
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
 * 后台系统商品分类管理
 *
 * @author Karas
 * @date 2012-3-8
 */
@Controller
@RequestMapping("/mm")
public class MaterialUomController {

    private static final Logger logger = LoggerFactory
            .getLogger(MaterialUomController.class);

    @Autowired
    private MaterialUomDao goodsUnitDao;

    /**
     * 查询商品分类列表
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/goodsUnitList/{goodsCode}", method = RequestMethod.GET)
    public Map<?, ?> selectGoodsUnits(@PathVariable("goodsCode") String goodsCode)
            throws BadRequestException {
        Map<String, Object> result = new HashMap<>();
        result.put("list", goodsUnitDao.selectByGoods(goodsCode));
        result.put("count", goodsUnitDao.countByGoods(goodsCode));
        return result;

    }

    /**
     * 通过主键ID查询商品分类详情
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/goodsUnit/{id}", method = RequestMethod.GET)
    public MaterialUom selectGoodsUnit(@PathVariable("id") String id)
            throws BadRequestException {
        MaterialUom goodsUnit = goodsUnitDao.selectOrInit(id);
        return goodsUnit;
    }

    /**
     * 更新商品分类
     *
     * @param goodsUnit
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/goodsUnitList/{goodsCode}", method = RequestMethod.POST)
    public void saveGoodsUnit(@PathVariable("goodsCode") String goodsCode, @RequestBody MaterialUom goodsUnit[])
            throws BadRequestException {
        for (MaterialUom unit : goodsUnit) {
            unit.setPn(goodsCode);
            goodsUnitDao.save(unit);
        }
    }

    /**
     * 删除商品分类
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/goodsUnit/{id}", method = RequestMethod.DELETE)
    public void deleteGoodsUnit(@PathVariable("id") String id)
            throws BadRequestException {
        if (goodsUnitDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");

    }

//    @RequestMapping(value = "/goodsUnitKV/{goodsCode}", method = RequestMethod.GET)
//    public Collection<MaterialUom> selectGoodsUnitsKV(@PathVariable("goodsCode") String goodsCode)
//            throws BadRequestException {
//        Collection<MaterialUom> list = goodsUnitDao.selectByGoods(goodsCode);
//        MaterialUom unit = goodsDao.selectByCode(goodsCode).getSkuUom();
//        Uom uom = uomDao.selectByCode(unit.getCode());
//        unit.setName(uom.getName());
//        unit.setPackQuantity(new BigDecimal(1));
//        list.add(unit);
//        return list;
//
//    }

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
    public ModelAndView otherRequestException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
