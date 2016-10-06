package com.goddess.ec.manage.model;

/**
 * 退货状态
 * @author bighua
 *
 */
public enum ReturnStatus {

	APPLIED("1", "已提交申请"),
	
    APPROVED("2", "已审核"),
	
    STORAGE("3", "已入库"),
	
    REFUNDED("4", "已退款"),
    
    UNUSUAL_FINISHED("5", "非正常完成");
	
	private String _code;
	private String _text;
	
	private ReturnStatus(String code, String text) {
		this._code = code;
		this._text = text;
	}
	
	public String getCode() {
		return _code;
	}
	
	public String getText() {
		return _text;
	}
	
	public static ReturnStatus getEnumByCode(String code) {
		ReturnStatus[] types = ReturnStatus.values();
		for (ReturnStatus t : types) {
			if (t.getCode().equals(code))
				return t;
		}
		return null;
	}
}
