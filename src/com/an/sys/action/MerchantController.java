package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.MerchantDao;
import com.an.sys.dao.RoleDao;
import com.an.sys.dao.UserDao;
import com.an.sys.entity.Merchant;
import com.an.sys.entity.User;
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
 * 商户
 */
@Controller
@RequestMapping("/sys")
public class MerchantController {

    private static final Logger logger = LoggerFactory
            .getLogger(MerchantController.class);

    @Autowired
    private MerchantDao merchantDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @RequestMapping(value = "/merchant/list", method = RequestMethod.GET)
    public Map<?, ?> search(WebRequest request) throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        Collection<Merchant> list = merchantDao.selectList(mParam);
        result.put("list", list);
        result.put("count", merchantDao.count(mParam));
        return result;
    }

    @RequestMapping(value = "/merchant", method = RequestMethod.POST)
    public Merchant insert(@RequestBody Merchant entity)
            throws BadRequestException {
        entity.setMerchCode(merchantDao.nextMerchantCode());
        if ("9".equals(entity.getMerchType())) {
            entity.setTemplate(null);
        }

        if (merchantDao.insert(entity) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return entity;
        }
    }

    @RequestMapping(value = "/merchant/{id}", method = RequestMethod.PUT)
    public Merchant update(@RequestBody Merchant entity)
            throws BadRequestException {
        if ("9".equals(entity.getMerchType())) {
            entity.setTemplate(null);
        }

        if (merchantDao.update(entity) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return entity;
        }
    }

    @RequestMapping(value = "/merchant/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") int id) throws BadRequestException {
        if (merchantDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");

        // TODO: 一系列的数据归档、删除操作
    }

    /**
     * 发布、启用商户
     *
     * @param merchId 商户ID
     * @throws BadRequestException
     */
    @RequestMapping(value = "/publishMerch/{merchId}", method = RequestMethod.GET)
    public void publish(@PathVariable("merchId") Integer merchId)
            throws BadRequestException {
        // TODO: 功能需要完成
        Merchant merch = merchantDao.selectOne(merchId);
        if ("1".equals(merch.getFlag())) {
            throw new BadRequestException("已初始化，不可重复执行");
        } else {
            User root = userDao.selectMerchRoot(merchId);

            if (root == null) root = new User(merch.getLoginName());
            root.setPassword(merch.getLoginPassword());
            root.setRealName(merch.getRealName());
            root.setEmail(merch.getUserEmail());
            root.setMobile(merch.getUserMobile());
            root.setStatus("t");
            root.setMerchant(merch);
            userDao.update(root);

            Merchant template;
            if (merch.getMerchType().equals("9")) {
                template = merch;
            } else {
                template = merchantDao.selectOne(merch.getTemplate());
            }
            roleDao.merchInit(merchId, root.getUserId(), template);
            merch.setFlag("1");
            merchantDao.update(merch);
        }
    }


    @RequestMapping(value = "/sys/kv/merch", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> selectKV()
            throws BadRequestException {
        return merchantDao.selectKV();
    }

    @RequestMapping(value = "/sys/kv/merchTemplate", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> selectTemplateKV()
            throws BadRequestException {
        return merchantDao.selectTemplateKV();
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
