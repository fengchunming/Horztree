package com.an.sys.dao;

import com.an.core.model.BaseDao;
import com.an.sys.entity.Module;
import com.an.sys.entity.User;
import com.an.utils.Digest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class UserDao extends BaseDao<User, Integer> {
	public UserDao() {
		super();
		namespace = "UserMapper";
	}

	public int save(User user) {
		if (user.getUserId() == null || user.getUserId() <= 0) {
			return insert(user);
		} else {
			return update(user);
		}
	}

	public boolean authority(String username, String password, String salt) {
		String pwd = sqlSession.selectOne(namespace + ".selectPassword", username);
		return pwd != null && Digest.SHA1(
						Digest.byte2Hex(Base64.decodeBase64(pwd)) + salt,
						Digest.Cipher.HEX).equals(password);
	}

	public boolean passwd(User user) {
		user.buildPwd();
		return sqlSession.update(namespace + ".passwd", user) == 1;
	}

	public User selectByUsername(String username) {
		return sqlSession.selectOne(namespace + ".selectUserByUsername", username);
	}

	public Collection<Module> selectMenus() {
		return sqlSession.selectList(namespace + ".selectMenus");
	}

	public Collection<Module> selectActions(int userId) {
		return sqlSession.selectList(namespace + ".selectActions", userId);
	}

	public Collection<Integer> selectRoles(int userId) {
		return sqlSession.selectList(namespace + ".selectRoles", userId);
	}

	public Collection<Map<String, String>> selectKV() {
		return sqlSession.selectList(namespace + ".selectKV");
	}

	public Collection<Map<String, String>> staffKV() {
		return sqlSession.selectList(namespace + ".staffKV");
	}

	public User selectMerchRoot(int merchId) {
		return sqlSession.selectOne(namespace + ".selectMerchRoot", merchId);
	}

	public int deleteRolesByUserId(Integer userId) {
		return sqlSession.delete(namespace + ".deleteRolesByUserId", userId);
	}

	public int insertRolesByUserId(Integer userId, Integer roleId) {
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("userId", userId);
		param.put("roleId", roleId);
		param.put("status", "t");
		return sqlSession.insert(namespace + ".insertRolesByUserId", param);
		
	}

}
