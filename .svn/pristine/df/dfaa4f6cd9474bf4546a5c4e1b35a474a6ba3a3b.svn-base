package com.goddess.ec.manage.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 后台用户角色
 * 
 * @author bighua
 *
 */
public enum AdminUserRole {

	SUPER_ADMIN("0", "超级管理员"),

	BUSINESS_ADMIN("1", "业务管理员"),
	
	BRAND_MANAGER("2", "品牌商");
	
	private String _code;
	private String _text;
	
	private AdminUserRole(String code, String text) {
		this._code = code;
		this._text = text;
	}
	
	public String getCode() {
		return _code;
	}
	
	public String getText() {
		return _text;
	}
	
	public static AdminUserRole getEnumByCode(String code) {
		AdminUserRole[] types = AdminUserRole.values();
		for (AdminUserRole t : types) {
			if (t.getCode().equals(code))
				return t;
		}
		return null;
	}
	
	public static JSONArray toJson() {
		AdminUserRole[] types = AdminUserRole.values();
		JSONArray arr = new JSONArray();
		for (AdminUserRole t : types) {
			JSONObject o = new JSONObject();
			o.put("code", t.getCode());
			o.put("text", t.getText());
			arr.add(o);
		}
		return arr;
	}
}
