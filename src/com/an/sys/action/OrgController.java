package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.OrganizationDao;
import com.an.sys.entity.Organization;
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
 * 后台组织管理
 *
 * @author Karas 2012-3-8
 */
@Controller
@RequestMapping("/sys")
public class OrgController {
    private static final Logger logger = LoggerFactory .getLogger(OrgController.class);
    
    @Autowired
    private OrganizationDao orgDao;

    /**
     * 查询组织列表
     * @param request
     * @param orgType（supplier-供应商）
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/org/{orgType}/list", method = RequestMethod.GET)
    public Map<?, ?> queryByOrgType(WebRequest request, @PathVariable(value="orgType") String orgType) throws BadRequestException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> mParam = Util.GetRequestMap(request);
        mParam.put("orgType", orgType);
        result.put("list", orgDao.selectList(mParam));
        result.put("count", orgDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/org/{orgType}/{id}", method = RequestMethod.GET)
    public Organization load(@PathVariable(value="id") Integer id) throws BadRequestException {
        return orgDao.selectOne(id);
    }

    @RequestMapping(value = "/org/{orgType}", method = RequestMethod.POST)
    public Organization insert(@RequestBody Organization org) throws BadRequestException {
        if (orgDao.insert(org) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
//            if ("ecstore".equals(org.getOrgType()))
//                apiService.loadStores();
            return org;
        }
    }

    @RequestMapping(value = "/org/{orgType}/{id}", method = RequestMethod.PUT)
    public Organization update(@RequestBody Organization org) throws BadRequestException {
        if (orgDao.update(org) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
//            if ("ecstore".equals(org.getOrgType()))
//                apiService.loadStores();
            return org;
        }
    }

    @RequestMapping(value = "/org/{orgType}/{id}", method = RequestMethod.DELETE)
    public void deleteOrg(@PathVariable(value="id") Integer id) throws BadRequestException {
        if (orgDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
//        else if ("ecstore".equals(orgType))
//            apiService.loadStores();
    }

    @RequestMapping(value = "/storeAutocomplete", method = RequestMethod.GET)
    public Collection<Organization> storeAutocomplete(@RequestParam String q, @RequestParam String limit) throws BadRequestException {
        Map<String, Object> mParam = new HashMap<>();
        mParam.put("_SH", q);
        mParam.put("_LM", limit);
        mParam.put("orgType", "store");
        return orgDao.selectList(mParam);
    }

    @RequestMapping(value = "/kv/org", method = RequestMethod.GET)
    public Collection<Map<String, String>> loadOrgKV() throws BadRequestException {
        return orgDao.selectKV(null);
    }

    @RequestMapping(value = "/kv/store", method = RequestMethod.GET)
    public Collection<Map<String, String>> selectStoreKV() throws BadRequestException {
        return orgDao.selectKV("store");
    }

//    @RequestMapping(value = "/kv/warehouse", method = RequestMethod.GET)
//    public Collection<Map<String, String>> selectWarehouseKV()
//            throws BadRequestException {
//        return orgDao.selectKV("warehouse");
//    }

    @RequestMapping(value = "/kv/supplier", method = RequestMethod.GET)
    public Collection<Map<String, String>> selectSupplierKV() throws BadRequestException {
        return orgDao.selectKV("supplier");
    }

    @RequestMapping(value = "/kv/ECStore", method = RequestMethod.GET)
    public Collection<Map<String, String>> selectECStoreKV() throws BadRequestException {
        return orgDao.selectKV("ecstore");
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
