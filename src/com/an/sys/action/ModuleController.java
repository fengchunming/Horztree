package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.ModuleDao;
import com.an.sys.entity.Module;
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
 * <p/>
 * Created by karas on 5/9/14.
 */
@Controller
@RequestMapping("/sys")
public class ModuleController {

    private static final Logger logger = LoggerFactory
            .getLogger(ModuleController.class);

    @Autowired
    private ModuleDao moduleDao;

    /**
     * 查询模块列表
     *
     * @param request 查询条件
     * @throws BadRequestException
     */
    @RequestMapping(value = "/module/list", method = RequestMethod.GET)
    public Map<?, ?> selectModules(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        Collection<Module> modules = moduleDao.selectList(mParam);
        result.put("list", modules);
        result.put("count", moduleDao.count(mParam));
        return result;
    }


    @RequestMapping(value = "/module/{id}", method = RequestMethod.GET)
    public Module selectModule(@PathVariable(value="id") Integer id)
            throws BadRequestException {
        return moduleDao.selectInit(id);
    }

    /**
     * 通过主键ID查询功能详情
     */
    @RequestMapping(value = "/kv/module", method = RequestMethod.GET)
    public Collection<Map<Integer, String>> selectModuleKV()
            throws BadRequestException {
        return moduleDao.selectKV();
    }

    /**
     * 保存系统功能
     */
    @RequestMapping(value = "/module", method = RequestMethod.POST)
    public Module insertModule(@RequestBody Module module)
            throws BadRequestException {
        if (moduleDao.save(module) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return module;
        }
    }

    /**
     * 保存系统功能
     */
    @RequestMapping(value = "/module/{id}", method = RequestMethod.PUT)
    public Module updateModule(@RequestBody Module module)
            throws BadRequestException {
        if (moduleDao.save(module) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            return module;
        }
    }

    /**
     * 删除功能
     *
     * @param id 功能ID
     * @throws BadRequestException
     */
    @RequestMapping(value = "/module/{id}", method = RequestMethod.DELETE)
    public void deleteModule(@PathVariable(value="id") int id) throws BadRequestException {
        if (moduleDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }

    /**
     * 异常处理
     *
     * @param e 异常
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
