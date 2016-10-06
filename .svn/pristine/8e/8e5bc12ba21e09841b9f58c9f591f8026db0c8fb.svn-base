package com.goddess.ec.manage.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.model.AdminUser;
import com.goddess.ec.manage.tools.ToolContext;
import com.goddess.ec.manage.tools.ToolMD5;
import com.goddess.ec.manage.tools.ToolSqlXml;

public class LoginService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(LoginService.class);

	public static final LoginService service = new LoginService();

	/**
	 * 用户登录后台验证
	 * 
	 * @param request
	 * @param response
	 * @param telephone
	 * @param password
	 * @param autoLogin
	 * @return
	 */
	public AdminUser login(HttpServletRequest request, HttpServletResponse response, String userName, String password,
	        boolean autoLogin) {
		// 1.取用户
//		AdminUser adminUser = null;
//		Object userObj = AdminUser.dao.cacheGet("user_name");
//		if (null != userObj) {
//			adminUser = (AdminUser) userObj;
//		} else {
			String sql = ToolSqlXml.getSql("manage.user.login");
			List<AdminUser> userList = AdminUser.dao.find(sql, userName);
			if (userList.size() != 1) {
				return null;// 用户不存在
			}
			AdminUser adminUser = userList.get(0);
//		}

//		// 2.停用账户
//		String deleteFlag = adminUser.getStr("DELETE_FLAG");
//		if (deleteFlag.equals("1")) {
//			return DictKeys.login_info_1;
//		}

		// 3.密码错误次数超限
//		int errorCount = AdminUser.getNumber("INVALID_COUNT").intValue();
//		int passErrorCount = (int) PropertiesPlugin.getParamMapValue(DictKeys.config_passErrorCount_key);
//		if (errorCount >= passErrorCount) {
//			Date stopDate = AdminUser.getDate("STOP_DATE");
//			int hourSpace = ToolDateTime.getDateHourSpace(ToolDateTime.getDate(), stopDate);
//			int passErrorHour = (int) PropertiesPlugin.getParamMapValue(DictKeys.config_passErrorHour_key);
//			if (hourSpace < passErrorHour) {
//				return DictKeys.login_info_2;// 密码错误次数超限，几小时内不能登录
//			} else {
//				String sql = ToolSqlXml.getSql("manage.AdminUser.start");
//				Db.update(sql, AdminUser.getStr("IDS"));
//				// 更新缓存
//				AdminUser.dao.cacheAdd(AdminUser.getStr("IDS"));
//			}
//		}

		// 4.验证密码
		boolean bool = ToolMD5.MD5(password).equalsIgnoreCase(adminUser.getStr("password"));
		if (bool) {
			// 密码验证成功
			ToolContext.setCurrentUser(request, response, adminUser, autoLogin);// 设置登录账户
//			return DictKeys.login_info_3;
			// 更新缓存
			AdminUser.dao.cacheAdd(adminUser.getInt("user_id").toString());
			return adminUser;
		} else {
			// 密码验证失败
//			String sql = ToolSqlXml.getSql("manage.AdminUser.stop");
//			Db.update(sql, ToolDateTime.getSqlTimestamp(ToolDateTime.getDate()), errorCount + 1, AdminUser.getStr("IDS"));
			// 更新缓存
//			AdminUser.dao.cacheAdd(adminUser.getStr("user_name") + "");
//			return DictKeys.login_info_4;
			return null;
		}
	}

}
