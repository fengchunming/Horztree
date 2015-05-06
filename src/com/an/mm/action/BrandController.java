package com.an.mm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.mm.dao.BrandDao;
import com.an.mm.entity.Brand;
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
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/mm")
public class BrandController {

    private static final Logger logger = LoggerFactory
            .getLogger(BrandController.class);

    @Autowired
    private BrandDao brandDao;

    @RequestMapping(value = "/brand/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", brandDao.selectList(mParam));
        result.put("count", brandDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/brand/{id}", method = RequestMethod.GET)
    public Brand load(@PathVariable(value="id") Integer id)
            throws BadRequestException {
        return brandDao.selectOne(id);
    }


    @RequestMapping(value = "/brand", method = RequestMethod.POST)
    public Brand insert(@RequestBody @Valid Brand brand, Errors rst)
            throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        }

        if (brandDao.insert(brand) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return brand;
        }
    }

    @RequestMapping(value = "/brand/{id}", method = RequestMethod.PUT)
    public Brand update(@RequestBody @Valid Brand brand, Errors rst) throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (brandDao.update(brand) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return brand;
        }

    }

    @RequestMapping(value = "/brand/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value="id") int id) throws BadRequestException {
        if (brandDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }

    @RequestMapping(value = "/kv/brand", method = RequestMethod.GET)
    public List loadKV() throws BadRequestException {
        return brandDao.selectKV();
    }


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
