package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.core.service.SessionRepository;
import com.an.sys.dao.UserDao;
import com.an.sys.entity.User;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 后台用户管理
 * <p>
 * created by Karas 2012-3-8
 */
@Controller
@RequestMapping("/sys")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserDao userDao;

    /**
     * 查询用户列表
     *
     * @param request 查询条件
     */
    @RequestMapping(value = "/user/list", method = RequestMethod.GET)
    public Map<?, ?> selectUsers(WebRequest request) throws BadRequestException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> mParam = Util.GetRequestMap(request);
        result.put("list", userDao.selectList(mParam));
        result.put("count", userDao.count(mParam));
        return result;
    }


    /**
     * 根据主键ID查询用户信息
     *
     * @param id 用户id
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User selectUser(@PathVariable(value="id") Integer id) throws BadRequestException {
        return userDao.selectOne(id);

    }

    /**
     * 保存用户
     */
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User insert(@RequestBody @Valid User user, Errors rst) throws BadRequestException {
        user.buildPwd();
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (userDao.insert(user) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            user.setPassword("");
            return user;
        }
    }

    /**
     * 保存用户
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    @Transactional
    public User update(@RequestBody @Valid User user, Errors rst) throws BadRequestException {
        if (rst.hasErrors()) {
            throw new BadRequestException(rst);
        } else if (userDao.update(user) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            if (user.getStatus().equals("f")) {
                SessionRepository.forceQuit(user.getId());
            }
            return user;
        }
    }

    /**
     * 删除用户
     */
    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable(value="id") int id) throws BadRequestException {
        if (userDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }

    /**
     * 修改用户密码
     */
    @RequestMapping(value = "/passwd", method = RequestMethod.POST)
    public void passwd(@RequestBody User user) throws BadRequestException {
        user.setFirstTime(true);
        if (!userDao.passwd(user))
            throw new BadRequestException("密码修改失败");
    }

    @RequestMapping(value = "/kv/user", method = RequestMethod.GET)
    public Collection<Map<String, String>> loadUserKV()
            throws BadRequestException {
        return userDao.selectKV();
    }


    @RequestMapping(value = "/kv/purchaser", method = RequestMethod.GET)
    public Collection<Map<String, String>> loadPurchaserKV()
            throws BadRequestException {
        return userDao.selectKV();
    }

    @RequestMapping(value = "/kv/staff", method = RequestMethod.GET)
    public Collection<Map<String, String>> loadStaffKV()
            throws BadRequestException {
        return userDao.staffKV();
    }


    @RequestMapping(value = "/user/isExistLoginName", method = RequestMethod.GET)
    @ResponseBody
    public boolean isExistSpaceName(@RequestParam(value = "userName") String userName,
                                    @RequestParam(value = "id") Integer id)
            throws BadRequestException {
        User user = userDao.selectByUsername(userName);
        return user == null || user.getUserId().equals(id);

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
