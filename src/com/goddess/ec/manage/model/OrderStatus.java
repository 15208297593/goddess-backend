package com.goddess.ec.manage.model;


public enum OrderStatus {

	UNPAID("0", "未支付"),

	UNSHIPPED("1", "生产中"),
	
    SHIPPED("2", "已发货"),
	
    SUCCESS("3", "已完成"),
	
    CANCEL("4", "已取消"),
    
	DEPOSIT_PAID("5", "订金已支付"),

    HIGHEND_CUSTOMIZING("6", "高端定制中"),
    
    RETURN("7", "已退货");
	
	private String _code;
	private String _text;
	
	private OrderStatus(String code, String text) {
		this._code = code;
		this._text = text;
	}
	
	public String getCode() {
		return _code;
	}
	
	public String getText() {
		return _text;
	}
	
	public static OrderStatus getEnumByCode(String code) {
		OrderStatus[] types = OrderStatus.values();
		for (OrderStatus t : types) {
			if (t.getCode().equals(code))
				return t;
		}
		return null;
	}
}
