package com.goddess.ec.manage.data;

import java.io.Serializable;

public class TotalData implements Serializable {

    private Total total;

    private Object data;

	public Total getTotal() {
		return total;
	}

	public void setTotal(Total total) {
		this.total = total;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
