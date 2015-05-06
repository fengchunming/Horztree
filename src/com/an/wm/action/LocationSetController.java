package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.utils.Util;
import com.an.wm.dao.LocationDao;
import com.an.wm.dao.LocationSetDao;
import com.an.wm.entity.Location;
import com.an.wm.entity.LocationSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
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
@RequestMapping("/wm")
public class LocationSetController {

    private static final Logger logger = LoggerFactory
            .getLogger(LocationSetController.class);

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private LocationSetDao locationSetDao;

    @RequestMapping(value = "/locationSet/{location}", method = RequestMethod.GET)
    public Map<?, ?> selectLocationSets(WebRequest request,
                                        @PathVariable("location") String location)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<String, Object>();
        mParam.put("location", location);
        result.put("list", locationSetDao.selectList(mParam));
        result.put("count", locationSetDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/locationSet/{location}", method = RequestMethod.POST)
    public void saveWmBillDetail(@RequestBody LocationSet sets[],
                                 @PathVariable("location") String location)
            throws BadRequestException {
        Location bill = locationDao.selectByCode(location);
        for (LocationSet set : sets) {
            if ("trash".equals(set.getStatus())) {
                locationSetDao.delete(set.getId());
            } else {
                set.setLocation(bill.getBarcode());
                set.setSection(bill.getSectionCode());
                set.setWarehouse(bill.getSectionCode());
                locationSetDao.save(set);
            }
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
    public ModelAndView requestException(Exception e) {
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

}
