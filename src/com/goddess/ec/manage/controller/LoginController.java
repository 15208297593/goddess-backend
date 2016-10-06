package com.goddess.ec.manage.controller;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.model.AdminUser;
import com.goddess.ec.manage.service.LoginService;
import com.goddess.ec.manage.tools.ToolContext;
import com.goddess.ec.manage.tools.ToolWeb;
import com.goddess.ec.manage.validator.LoginValidator;
import com.jfinal.aop.Before;

/**
 * 登陆处理
 */
@Controller(controllerKey = "/admin/login")
public class LoginController extends BaseController {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(LoginController.class);
	
	/**
	 * 准备登陆
	 */
	public void index() {
		AdminUser adminUser = ToolContext.getCurrentUser(getRequest(), true); // cookie认证自动登陆处理
		if(null != adminUser){//后台
			setAttr("userName", adminUser.getStr("user_name"));
			setAttr("role_brand_id", adminUser.getInt("brand_id"));
			setAttr("role", adminUser.getStr("role"));
			render("/menu.html");
		}else{
			System.out.println("adminUser is null");
			render("/login.html");
		}
	}
	
	/**
	 * 登陆验证
	 */
	@Before(LoginValidator.class)
	public void vali() {
//		boolean authCode = authCode();
//		if(authCode){
			String userName = getPara("user_name");
			String password = getPara("password");
			String remember = getPara("remember");
			boolean autoLogin = false;
			if(null != remember && remember.equals("1")){
				autoLogin = true;
			}
			AdminUser adminUser = LoginService.service.login(getRequest(), getResponse(), userName, password, autoLogin);
			if(adminUser != null){
				setAttr("userName", userName);
				setAttr("role_brand_id", adminUser.getInt("brand_id"));
				setAttr("role", adminUser.getStr("role"));
				render("/menu.html");
				return;
			}
//		}
		
		setAttr("telMsg", "用户名或密码不正确");
		redirect("/admin/login");
	}

	/**
	 * 注销
	 */
	public void logout() {
		ToolWeb.addCookie(getResponse(), "", "/", true, "authmark", null, 0);
		redirect("/admin/login");
	}

}
