package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "property_type", pkName = "property_type_id")
public class PropertyType extends BaseModel<PropertyType> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 8846832264052756614L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(PropertyType.class);

    public static final PropertyType dao = new PropertyType();

}
