package com.goddess.ec.manage.data;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.jfinal.kit.JsonKit;


public class Delivery {

	private String status = "";
	@JsonIgnore
	private String billstatus = "";
	private String message = "";
	private DeliveryResult lastResult = new DeliveryResult();

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBillstatus() {
		return billstatus;
	}

	public void setBillstatus(String billstatus) {
		this.billstatus = billstatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DeliveryResult getLastResult() {
		return lastResult;
	}

	public void setLastResult(DeliveryResult lastResult) {
		this.lastResult = lastResult;
	}

	@Override
	public String toString() {
		return JsonKit.toJson(this);
	}
}
