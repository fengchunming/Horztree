package com.an.pos.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.pos.dao.CashierDao;
import com.an.pos.dao.TerminalDao;
import com.an.pos.entity.Cashier;
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
 * 后台系统功能模块管理
 *
 * @author Karas
 * @date 2012-3-8
 */
@Controller
@RequestMapping("/pos")
public class CashierController {

    private static final Logger logger = LoggerFactory
            .getLogger(CashierController.class);

    @Autowired
    private CashierDao cashierDao;

    @Autowired
    private TerminalDao terminalDao;

    // @Autowired
    //DigiSender sender;

    /**
     * 查询模块列表
     *
     * @param request
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/cashierList", method = RequestMethod.GET)
    public Map<?, ?> selectCashiers(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        Collection<Cashier> cashiers = cashierDao.selectList(mParam);
        result.put("list", cashiers);
        result.put("count", cashierDao.count(mParam));
        return result;
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @param id
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/cashier/{id}", method = RequestMethod.GET)
    public Cashier selectCashier(@PathVariable(value="id") String id)
            throws BadRequestException {
        return cashierDao.selectOne(id);
    }

    /**
     * 通过主键ID查询功能详情
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/loadCashierKV.do", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> selectCashierKV()
            throws BadRequestException {
        return cashierDao.selectKV();
    }

    /**
     * 更新功能
     *
     * @param cashier
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/cashier", method = RequestMethod.POST)
    public Cashier insertCashier(@RequestBody Cashier cashier)
            throws BadRequestException {
        if (cashierDao.insert(cashier) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return cashier;
        }
    }

    @RequestMapping(value = "/cashier/{id}", method = RequestMethod.PUT)
    public Cashier updateCashier(@RequestBody Cashier cashier)
            throws BadRequestException {
        if (cashierDao.update(cashier) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return cashier;
        }
    }

    /**
     * 删除功能
     *
     * @return
     * @throws BadRequestException
     */
    @RequestMapping(value = "/cashier/{id}", method = RequestMethod.DELETE)
    public void deleteCashier(@PathVariable(value="id") String id)
            throws BadRequestException {
        if (cashierDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }

//    /**
//     * 通过主键ID查询商品分类详情
//     *
//     * @return
//     * @throws BadRequestException
//     */
//    @RequestMapping(value = "/sendCashier", method = RequestMethod.POST)
//    @ResponseBody
//    public String sendCashier(@RequestBody Cashier[] cashiers)
//            throws BadRequestException {
//
//        Collection<Cashier> cashierList = cashierDao
//                .selectList(new HashMap<String, Object>());
//        sender.sendAll(cashierList);
//
//        return "发送成功";
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
    public ModelAndView otherException(Exception e) {
        logger.warn(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }
}
