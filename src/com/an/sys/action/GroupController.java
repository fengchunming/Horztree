package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.GroupDao;
import com.an.sys.entity.Group;
import com.an.trade.dao.TradeDao;
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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/sys")
public class GroupController {
    private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private TradeDao tradeDao;

    /**
     * 分类查询所有网点
     * @param type w-总仓,s-网点,m-夜猫店
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/group/{type}/list", method = RequestMethod.GET)
    public Map<?, ?> query(@PathVariable(value="type") String type, WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        if (!"_".equals(type)) mParam.put("type", type);
        result.put("list", groupDao.selectList(mParam));
        result.put("count", groupDao.count(mParam));
        return result;
    }

    /**
     * 分类查询当前用户所属的所有网点
     * @param type w-总仓,s-网点,m-夜猫店
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/group/{type}/own", method = RequestMethod.GET)
    public Map<?, ?> queryOwn(@PathVariable(value="type")  String type, WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        mParam.put("own", true);
        mParam.put("type", type);
        result.put("list", groupDao.selectList(mParam));
        result.put("count", groupDao.count(mParam));
        return result;
    }

    /**
     * 查询当前用户所属的所有网点
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/group/own", method = RequestMethod.GET)
    public Map<?, ?> queryAllOwn(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        mParam.put("own", true);
        result.put("list", groupDao.selectList(mParam));
        result.put("count", groupDao.count(mParam));
        return result;
    }


    /**
     * 通过主键ID查询部门详情
     */
    @RequestMapping(value = "/group/{type}/{id}", method = RequestMethod.GET)
    public Group load(@PathVariable(value="type")  Integer id)
            throws BadRequestException {
        return groupDao.selectOne(id);
    }

    /**
     * 新增部门
     */
    @RequestMapping(value = "/group/{type}", method = RequestMethod.POST)
    public Group insert(@RequestBody @Valid Group group, Errors rst)
            throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (groupDao.save(group) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return group;
        }
    }

    /**
     * 更新部门
     */
    @RequestMapping(value = "/group/{type}/{id}", method = RequestMethod.PUT)
    public Group update(@RequestBody @Valid Group group, Errors rst)
            throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (groupDao.save(group) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return group;
        }
    }

    /**
     * 删除部门
     */
    @RequestMapping(value = "/group/{type}/{id}", method = RequestMethod.DELETE)
    public void deleteModule(@PathVariable(value="id")  int id) throws BadRequestException {
    	if(tradeDao.countByGroupId(id)>0){
    		throw new BadRequestException("此网点下有订单,不能删除！");
    	}
    	else if (groupDao.delete(id) <= 0){
            throw new BadRequestException("删除失败");
        }
    }

    @RequestMapping(value = "/kv/warehouse", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> loadWarehouse()
            throws BadRequestException {
        return groupDao.selectKV("w", false);
    }

    @RequestMapping(value = "/kv/group", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> loadKV()
            throws BadRequestException {
        return groupDao.selectKV(null, false);
    }

    @RequestMapping(value = "/kv/owngroup", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> loadOwnKV() throws BadRequestException {
        return groupDao.selectKV(null, true);
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
