package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "user_customization", pkName = "user_customization_id")
public class UserCustomization extends BaseModel<UserCustomization> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3699125067337151799L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(UserCustomization.class);

    public static final UserCustomization dao = new UserCustomization();

}
