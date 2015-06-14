package com.an.sys.entity;

import com.an.base.entity.Region;
import com.an.utils.Digest;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 用户，员工
 *
 * @author Karas
 */

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	public static final String SESSION_KEY = "USER_SESSION_KEY";

	private Integer userId; // 用户ID
	private String userName; // 用户名称
	private String password; // 密码
	private String staffCode; // 人事员工号
	private String realName; // 真实姓名
	private String email;
	private String mobile;
	private String remark;
	private Organization org = new Organization();
	private List<Integer> groups = new ArrayList<>();
	private String token = " "; // 识别号
	private String status = "t"; // 状态 d:已删除,t:启用,f:停用
	private Integer createBy;
	private Date createAt;
	private boolean firstTime = true; // 密码状态

	private Merchant merchant;

	private List<Integer> roles = new ArrayList<>();

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private HashSet<String> c = new HashSet<>();
	private HashSet<String> r = new HashSet<>();
	private HashSet<String> u = new HashSet<>();
	private HashSet<String> d = new HashSet<>();
	private HashSet<String> b = new HashSet<>();
	
	private Integer regionId;
	private Region region;

	public User() {
	}

	public User(Integer userId) {
		this.userId = userId;
	}

	public User(String staffCode) {
		this.staffCode = staffCode;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Organization getOrg() {
		return org;
	}

	public void setOrg(Organization org) {
		this.org = org;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Integer createBy) {
		this.createBy = createBy;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}

	public boolean isFirstTime() {
		return firstTime;
	}

	public void setFirstTime(boolean firstTime) {
		this.firstTime = firstTime;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public boolean permit(String action, String method) {
		switch (method) {
		case "POST":
			return c.contains(action) || regex(c, action);
		case "GET":
			return r.contains(action) || regex(r, action);
		case "PUT":
			return u.contains(action) || regex(u, action);
		case "DELETE":
			return d.contains(action) || regex(d, action);
		default:
			return false;
		}
	}

	private boolean regex(HashSet<String> set, String act) {
		for (String reg : set) {
			if (Pattern.matches("^" + reg + "$", act))
				return true;
		}
		return false;
	}

	private void split(HashSet<String> source, String urls) {
		if (urls.matches("(.*)")) {
			for (String url : urls.split("[,;]")) {
				if (url.trim().length() > 0)
					source.add(url.trim());
			}
		} else {
			source.add(urls.trim());
		}
	}

	public void addAction(Module action) {
		if (action.getUrl() != null) {
			switch (action.getMethod()) {
			case "GET":
				split(r, action.getUrl());
				break;
			case "POST":
				split(c, action.getUrl());
				break;
			case "DELETE":
				split(d, action.getUrl());
				break;
			case "PUT":
				split(u, action.getUrl());
				break;
			}
		}
		if (action.getButtonId() != null) {
			this.b.add(action.getButtonId());
		}
	}

	public List<Integer> getRoles() {
		return roles;
	}
	
	public List<Integer> setRoles(List<Integer> roles) {
		return this.roles = roles;
	}

	public void addRole(Integer role) {
		this.roles.add(role);
	}

	public HashSet<String> getButtons() {
		return b;
	}

	public void buildPwd() {
		this.password = Digest.hex2Base64(this.password);
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public List<Integer> getGroups() {
		return groups;
	}

	public void setGroups(List<Integer> groups) {
		this.groups = groups;
	}

}
