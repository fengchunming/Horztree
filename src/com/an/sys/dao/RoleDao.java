package com.an.sys.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.Merchant;
import com.an.sys.entity.Module;
import com.an.sys.entity.Role;
import com.an.sys.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class RoleDao extends BaseDao<Role, Integer> {
    public RoleDao() {
        super();
        namespace = "RoleMapper";
    }

    public Role selectInit(Integer primaryKey) {
        if (primaryKey != null && primaryKey > 0)
            return selectOne(primaryKey);
        else
            return new Role();
    }

    public int save(Role role) {
        if (role.getRoleId() != null && role.getRoleId() > 0)
            return update(role);
        else
            return insert(role);

    }

    public List<Integer> selectGrantedModule(int roleId) {
        return sqlSession
                .selectList(namespace + ".selectGrantedModule", roleId);
    }

    public List<Map<?, ?>> selectGrantedUser(int roleId) {
        return sqlSession.selectList(namespace + ".selectGrantedUser", roleId);
    }

    public int saveGrantedModule(Module module, Integer roleId) {
        Map<String, Object> granted = new HashMap<>();
        granted.put("moduleId", module.getId());
        granted.put("roleId", roleId);
        granted.put("status", module.getStatus());
        if (updateGrantedModule(granted) == 0 && module.getStatus().equals("t")) {
            insertGrantedModule(granted);
        }
        return 1;
    }

    public int saveGrantedUser(User user, Integer roleId) {
        Map<String, Object> granted = new HashMap<>();
        granted.put("userId", user.getUserId());
        granted.put("roleId", roleId);
        granted.put("status", user.getStatus());
        if (updateGrantedUser(granted) == 0 && user.getStatus().equals("t")) {
            insertGrantedUser(granted);
        }
        return 1;
    }

    /**
     * 增加功能授权
     */
    private int insertGrantedModule(Map<String, Object> granted) {
        return sqlSession.insert(namespace + ".insertGrantedModule", granted);
    }

    /**
     * 更新功能授权状态 有效/失效
     */
    private int updateGrantedModule(Map<String, Object> granted) {
        return sqlSession.update(namespace + ".updateGrantedModule", granted);
    }

    /**
     * 增加用户授权
     */
    private int insertGrantedUser(Map<String, Object> granted) {
        return sqlSession.insert(namespace + ".insertGrantedUser", granted);
    }

    /**
     * 更新用户授权状态 有效/失效
     */
    private int updateGrantedUser(Map<String, Object> granted) {
        return sqlSession.update(namespace + ".updateGrantedUser", granted);
    }


    public void merchInit(Integer merchId, Integer userId, Merchant template) {
        // TODO: 功能需要完成
        /*
            给root用户一个指定角色
         */
        Map<String, Object> roleUser = new HashMap<>();
        roleUser.put("id", userId);
        roleUser.put("roleId", template.getRoot());
        roleUser.put("status", "t");
        insertGrantedUser(roleUser);

        /*
            根据模版给商户增加角色，及角色权限
         */
        Map<String, Object> param = new HashMap<>();
        Map<String, Object> granted = new HashMap<>();
        param.put("merch", template.getId());
        Collection<Role> roles = selectList(param);
        for (Role role : roles) {
            Integer origin = role.getRoleId();
            role.setMerchantId(merchId);
            role.setRoleId(0);
            insert(role);

            granted.put("roleId", role.getRoleId());
            granted.put("origin", origin);
            insertGrantedModule(granted);
        }

    }

}
