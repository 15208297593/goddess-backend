package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "material_type", pkName = "material_type_id")
public class MaterialType extends BaseModel<MaterialType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 94646301015340838L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(MaterialType.class);

    public static final MaterialType dao = new MaterialType();

}
