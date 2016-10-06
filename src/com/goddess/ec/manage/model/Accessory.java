package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "accessory", pkName = "accessory_id")
public class Accessory extends BaseModel<Accessory> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1261801310854456773L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(Accessory.class);

    public static final Accessory dao = new Accessory();

}
