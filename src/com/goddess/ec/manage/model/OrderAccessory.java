package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "order_accessory", pkName = "order_accessory_id")
public class OrderAccessory extends BaseModel<OrderAccessory> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7561613829109407664L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(OrderAccessory.class);

    public static final OrderAccessory dao = new OrderAccessory();

}
