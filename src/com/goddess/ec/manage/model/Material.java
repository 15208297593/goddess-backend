package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "materials", pkName = "material_id")
public class Material extends BaseModel<Material> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1261801310854456773L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(Material.class);

    public static final Material dao = new Material();

}
