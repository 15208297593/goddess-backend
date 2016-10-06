package com.goddess.ec.manage.validator;

import org.apache.log4j.Logger;

import com.jfinal.core.Controller;

public class LoginValidator extends BaseValidator {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(LoginValidator.class);

	protected void validate(Controller controller) {
		validateString("user_name", 4, 11, "telMsg", "请输入用户名!");
		validateString("password", 6, 20, "passwordMsg", "请输入密码!");
	}

	protected void handleError(Controller controller) {
		controller.keepModel(LoginValidator.class);

		String actionKey = getActionKey();
		if (actionKey.equals("/admin/login/vali")) {
			controller.render("/login.html");
		}
	}

}
