/**
 *
 */
package com.an.sys.action;

import com.an.core.exception.BadRequestException;
import com.an.core.exception.ErrorModelAndView;
import com.an.sys.dao.RoleDao;
import com.an.sys.entity.Module;
import com.an.sys.entity.Role;
import com.an.sys.entity.User;
import com.an.utils.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色，权限管理
 *
 * @author Karas
 * @date 2012-3-8
 */

@Controller
@RequestMapping("/sys")
public class RoleController {
    private static final Logger logger = LoggerFactory
            .getLogger(RoleController.class);
    @Autowired
    private RoleDao roleDao;

    /**
     * 查询角色列表
     *
     * @param request
     * @return
     * @throws com.an.core.exception.BadRequestException
     */
    @RequestMapping(value = "/role/list", method = RequestMethod.GET)
    public Map<String, Object> selectRoles(WebRequest request)
            throws BadRequestException {
        Map<String, Object> mParam = Util.GetRequestMap(request);
        Map<String, Object> result = new HashMap<>();
        Collection<Role> roles = roleDao.selectList(mParam);
        result.put("list", roles);
        result.put("count", roleDao.count(mParam));
        return result;
    }

    /**
     * 根据主键ID查询角色信息
     *
     * @param id
     * @return
     * @throws com.an.core.exception.BadRequestException
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.GET)
    public Role selectRole(@PathVariable(value="id") Integer id) throws BadRequestException {
        return roleDao.selectInit(id);

    }

    /**
     * 新增角色
     *
     * @param role
     * @return 返回修改后的角色
     * @throws com.an.core.exception.BadRequestException
     */
    @RequestMapping(value = "/role", method = RequestMethod.POST)
    @Transactional
    public Role insertRole(@RequestBody Role role) throws BadRequestException {
        if (roleDao.save(role) != 1) {
            throw new BadRequestException("保存失败！");
        } else {
            for (User user : role.getUsers()) {
                roleDao.saveGrantedUser(user, role.getRoleId());
            }
            for (Module module : role.getModules()) {
                roleDao.saveGrantedModule(module, role.getRoleId());
            }
            return role;
        }
    }

    @RequestMapping(value = "/role/{id}", method = RequestMethod.PUT)
    public Role updateRole(@RequestBody Role role) throws BadRequestException {
        return insertRole(role);
    }

    /**
     * 删除角色
     *
     * @throws com.an.core.exception.BadRequestException
     */
    @RequestMapping(value = "/role/{id}", method = RequestMethod.DELETE)
    public void deleteRole(@PathVariable(value="id") int id) throws BadRequestException {
        if (roleDao.delete(id) <= 0)
            throw new BadRequestException("删除失败");
    }

    /**
     * 查询角色权限
     *
     * @param roleId
     * @return
     * @throws com.an.core.exception.BadRequestException
     */
    @RequestMapping(value = "/grantedModule/{id}", method = RequestMethod.GET)
    public List<Integer> loadGrantedModule(@PathVariable("id") int roleId)
            throws BadRequestException {
        return roleDao.selectGrantedModule(roleId);

    }

    /**
     * 查询角色授权用户
     *
     * @param roleId 角色ID
     * @return 返回该角色下的用户
     * @throws com.an.core.exception.BadRequestException
     */
    @RequestMapping(value = "/grantedUser/{id}", method = RequestMethod.GET)
    public List<Map<?, ?>> loadGrantedUser(@PathVariable("id") Integer roleId)
            throws BadRequestException {
        return roleDao.selectGrantedUser(roleId);
    }

    /**
     * 异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView bdRequestException(Exception e) {
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
