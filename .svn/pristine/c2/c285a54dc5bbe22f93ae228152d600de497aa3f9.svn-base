package com.goddess.ec.manage.data;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.jfinal.kit.JsonKit;

public class DeliveryResult {

	private String message = "";
	private String nu = "";
	private String ischeck = "0";
	private String com = "";
	@JsonIgnore
	private String status = "0";
	private ArrayList<DeliveryRoute> data = new ArrayList<DeliveryRoute>();
	private String state = "0";
	@JsonIgnore
	private String condition = "";

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public ArrayList<DeliveryRoute> getData() {
		return data;
	}

	public void setData(ArrayList<DeliveryRoute> data) {
		this.data = data;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public String toString() {
		return JsonKit.toJson(this);
	}
}
