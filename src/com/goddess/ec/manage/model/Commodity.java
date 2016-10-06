package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "commodities", pkName = "commodity_id")
public class Commodity extends BaseModel<Commodity> {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2460990625220499730L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(Commodity.class);

    public static final Commodity dao = new Commodity();

}
