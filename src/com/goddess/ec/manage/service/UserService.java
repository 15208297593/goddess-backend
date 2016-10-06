package com.goddess.ec.manage.service;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.plugin.activerecord.Db;

public class UserService extends BaseService {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(UserService.class);

	public static final UserService service = new UserService();

	/**
	 * 根据检索条件
	 *
	 */
	public void getUsers(SplitPage splitPage) {
		splitPageBase(splitPage, "select *", "manage.user.splitPage");
	}

	/**
	 * 奖励积分
	 * @param userId
	 * @param points
	 */
	public boolean awardPoints(int userId, int points) {
    	return Db.update(ToolSqlXml.getSql("manage.user.awardPoints"), points, userId) != 0;
	}
}
