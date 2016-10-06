package com.goddess.ec.manage.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * logo印制类别
 * 
 * @author bighua
 *
 */
public enum LogoType {

	INNER_LOGO("0", "内牌", "0"),
	
	HANG_LOGO("1", "吊牌", "1");
	
	private String _code;
	private String _text;
	private String _isMulti;
	
	private LogoType(String code, String text, String isMulti) {
		this._code = code;
		this._text = text;
		this._isMulti = isMulti;
	}
	
	public String getCode() {
		return _code;
	}
	
	public String getText() {
		return _text;
	}
	
	public String isMulti() {
		return _isMulti;
	}
	
	public static LogoType getEnumByCode(String code) {
		LogoType[] types = LogoType.values();
		for (LogoType t : types) {
			if (t.getCode().equals(code))
				return t;
		}
		return null;
	}
	
	public static JSONArray toJson() {
		LogoType[] types = LogoType.values();
		JSONArray arr = new JSONArray();
		for (LogoType t : types) {
			JSONObject o = new JSONObject();
			o.put("code", t.getCode());
			o.put("text", t.getText());
			o.put("isMulti", t.isMulti());
			arr.add(o);
		}
		return arr;
	}
}
