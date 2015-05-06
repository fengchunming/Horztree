package com.an.wm.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.utils.Util;
import com.an.wm.dao.LocationDao;
import com.an.wm.entity.Location;
import com.an.wm.entity.PossibleStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

/**
 * 储位管理
 *
 * @author Karas 2012-3-8
 */
@Controller
@RequestMapping("/wm")
public class LocationController {

    private static final Logger logger = LoggerFactory
            .getLogger(LocationController.class);


    @Autowired
    private LocationDao locationDao;


    @RequestMapping(value = "/location/list", method = RequestMethod.GET)
    public Map<?, ?> query(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        result.put("list", locationDao.selectList(mParam));
        result.put("count", locationDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/location/{id}", method = RequestMethod.GET)
    public Location load(@PathVariable("id") Integer id)
            throws BadRequestException {
        return locationDao.selectOrInit(id);
    }

    @RequestMapping(value = "/location", method = RequestMethod.POST)
    public Location insert(@RequestBody Location location)
            throws BadRequestException {
        if (locationDao.insert(location) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return location;
        }
    }

    @RequestMapping(value = "/location/{id}", method = RequestMethod.PUT)
    public Location update(@RequestBody Location location)
            throws BadRequestException {
        if (locationDao.insert(location) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return location;
        }
    }

    @RequestMapping(value = "/location/{id}", method = RequestMethod.DELETE)
    public void deleteLocation(@PathVariable("id") Integer id)
            throws BadRequestException {
        if (locationDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }

    /**
     * 打印货架条码
     * @param ids
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/location/print/{ids}", method = RequestMethod.GET)
    public ModelAndView printLocation(@PathVariable("ids") String ids)
            throws BadRequestException {
        ModelAndView mav = new ModelAndView("report/locationBarcode");
        Map<String, Object> param = new HashMap<>();

        if (ids.length() > 0) {
            String str[] = ids.split("-");
            int ints[] = new int[str.length];
            for (int i = 0; i < str.length; i++) {
                ints[i] = Integer.parseInt(str[i]);
            }
            param.put("ids", ints);
        } else {
            param.put("status", "enabled");
        }
        Collection<Location> locations = locationDao.selectList(param);
        mav.addObject("locations", locations);
        return mav;
    }

    /**
     * 批量生成货位
     * @param location
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/location/{id}", method = RequestMethod.POST)
    public Location saveLocation(@RequestBody Location location)
            throws BadRequestException {
        if (location.getId() == null || location.getId() == 0) {
            List<String[]> list = new ArrayList<String[]>();
            String[] sec = location.getBarcode().split(";");

            if (sec.length > 1) {
                for (String x : sec) {
                    String[] z = null;
                    if (x.contains(",")) {
                        z = x.split(",");
                    } else if (x.contains("~")) {
                        String[] y = x.split("~");
                        // if(y[1].matches("\\d")){
                        String format = "%0" + y[1].length() + "d";
                        z = new String[Integer.parseInt(y[1])
                                - Integer.parseInt(y[0]) + 1];
                        for (int i = 0; i < z.length; i++) {
                            z[i] = String.format(format, Integer.parseInt(y[0])
                                    + i);
                        }
                        // }
                        y[1].length();
                    } else {
                        z = new String[]{x};
                    }
                    list.add(z);
                }
                List<String> result = new ArrayList<String>();
                Descartes(list, 0, result, "");
                for (String barcode : result) {
                    location.setId(0);
                    location.setBarcode(barcode);
                    locationDao.insert(location);
                }
            } else {
                locationDao.insert(location);
            }
        } else {
            locationDao.save(location);
        }
        return location;
    }

    private static String Descartes(List<String[]> list, int count,
                                    List<String> result, String data) {
        String temp = data;
        String[] astr = list.get(count);
        for (int i = 0; i < astr.length; i++) {
            if (count + 1 < list.size()) {
                Descartes(list, count + 1, result, data + astr[i]);
            } else {
                result.add(data + astr[i]);
            }
        }
        return temp;
    }


    @RequestMapping(value = "/kv/location", method = RequestMethod.GET)
    public Collection<Map<String, String>> selectLocation()
            throws BadRequestException {
        return locationDao.selectKV();
    }


    @RequestMapping(value = "/location/filter", method = RequestMethod.GET)
    public Collection<Map<String, String>> selectLocationBySection(
            PossibleStorage poss) throws BadRequestException {
        return locationDao.possibleLocation(poss);
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
        logger.error(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }
}
