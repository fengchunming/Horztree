package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.core.exception.NotAcceptableException;
import com.an.core.exception.UnauthorizedException;
import com.an.core.service.RecaptchaServlet;
import com.an.core.service.SessionRepository;
import com.an.sys.dao.OrganizationDao;
import com.an.sys.dao.UserDao;
import com.an.sys.entity.Module;
import com.an.sys.entity.User;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
@Controller
@RequestMapping("/sec")
public class GrantedController {

    private static final Logger logger = LoggerFactory
            .getLogger(GrantedController.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private OrganizationDao orgDao;


    /**
     * 身份验证
     *
     * @param username  用户名
     * @param password  密码
     * @param recaptcha 图片验证码
     * @throws UnauthorizedException
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public User doAuth(HttpSession session, HttpServletRequest request,
                       @RequestParam("username") String username,
                       @RequestParam("password") String password,
                       @RequestParam("recaptcha") String recaptcha,
                       @RequestParam("force") String force)
            throws UnauthorizedException, NotAcceptableException {

        String imgCheck = Util.nvl(
                session.getAttribute(RecaptchaServlet.SESSION_KEY), "N/A");

        // 验证码是否正确
        if (!recaptcha.equalsIgnoreCase(imgCheck)) {
            session.removeAttribute(RecaptchaServlet.SESSION_KEY);
            throw new UnauthorizedException("验证码错误!");
        }

        // 用户名,密码是否正确. imgCheck即图片验证码，在密码验证中起SALT作用。
        if (!userDao.authority(username, password, recaptcha)) {
            session.removeAttribute(RecaptchaServlet.SESSION_KEY);
            throw new UnauthorizedException("身份验证失败!");
        }

        User user = userDao.selectByUsername(username);
       /* if (SessionRepository.isOnline(user.getId(), session)) {
            switch (force) {
                case "true":
                    SessionRepository.forceQuit(user.getId());
                    break;
                case "false":
                    break;
                default:
                    throw new NotAcceptableException("发现账号已在其它地方登录，是否强制其退出？");
            }
        }*/

        user.setOrg(orgDao.selectInit(user.getOrg().getId()));
        for (Module mod : userDao.selectActions(user.getUserId())) {
            user.addAction(mod);
        }

        for (Integer roleId : userDao.selectRoles(user.getUserId())) {
            user.addRole(roleId);
        }

//        System.out.println(request.getUserPrincipal().getName());
        session.setAttribute(User.SESSION_KEY, user);
//        Cookie xsrf = new Cookie("_xsrf", UUID.randomUUID().toString());
//        session.setAttribute("CSRF_SALT_CACHE", xsrf.getValue());
//        response.addCookie(xsrf);
 //       SessionRepository.add(user.getId(), session.getId());
//        logger.info("LoginSuccess: ID: " + username);
        return user;

    }

    /**
     * 用户session查询
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User getUser(HttpSession session) {
        return (User) session.getAttribute(User.SESSION_KEY);
    }

    /**
     * 用户菜单查询
     */
    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public Map<String, Object> menu(HttpSession session) {
        User user = (User) session.getAttribute(User.SESSION_KEY);
        Collection<Module> list = userDao.selectMenus();
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("menu", list);

        return map;
    }

    /**
     * 用户退出登录
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelAndView logout(HttpSession session) {
        //SessionRepository.remove(session.getId());
        session.invalidate();
        return new ModelAndView("redirect:/?_=" + Util.RunTimeSequence());
    }

    /**
     * 用户修改密码
     *
     * @param session   用户session
     * @param oldPasswd 原密码
     * @param newPasswd 新密码
     * @throws UnauthorizedException
     */
    @RequestMapping(value = "/passwd", method = RequestMethod.POST)
    public void updatePWD(HttpSession session,
                          @RequestParam("oldPasswd") String oldPasswd,
                          @RequestParam("newPasswd") String newPasswd) throws Exception {
        User user = (User) session.getAttribute(User.SESSION_KEY);
        if (!user.isFirstTime() && !userDao.authority(user.getUserName(), oldPasswd, newPasswd)) {
            throw new BadRequestException("身份验证失败，旧密码不正确！");
        }
        user.setPassword(newPasswd);
        user.setFirstTime(false);
        if (!userDao.passwd(user)) {
            throw new BadRequestException("密码修改失败！");
        }
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public ModelAndView authException(Exception e) {
        logger.warn(e.getMessage());
        return new ErrorModelAndView(e);
    }

    @ExceptionHandler(NotAcceptableException.class)
    @ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
    public ModelAndView notAcceptAble(Exception e) {
        logger.warn(e.getMessage());
        return new ErrorModelAndView(e);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequestException(Exception e) {
        logger.warn(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView otherException(Exception e) {
        logger.warn(e.getMessage(), e);
        return new ErrorModelAndView(e);
    }
}
