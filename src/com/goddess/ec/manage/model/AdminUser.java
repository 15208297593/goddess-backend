package com.goddess.ec.manage.model;

import java.util.List;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;
import com.goddess.ec.manage.common.DictKeys;
import com.goddess.ec.manage.thread.ThreadParamInit;
import com.jfinal.plugin.ehcache.CacheKit;

@Table(tableName = "admin_users", pkName="user_id")
public class AdminUser extends BaseModel<AdminUser> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4228707163498175070L;

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(AdminUser.class);

	public static final AdminUser dao = new AdminUser();

	/**
	 * 添加或者更新缓存
	 */
	public void cacheAdd(String ids) {
		AdminUser user = AdminUser.dao.findById(ids);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + ids, user);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("cell_num"), user);
	}

	/**
	 * 添加多个用户到缓存中
	 * @param allUsers
	 */
	public void cacheAddAll(List<AdminUser> allUsers) {
		for (AdminUser user : allUsers) {
			CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getInt("user_id"), user);
			CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("cell_num"), user);
		}
	}

	/**
	 * 删除缓存
	 */
	public void cacheRemove(String ids) {
		AdminUser user = AdminUser.dao.findById(ids);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + ids);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("cell_num"));
	}

	/**
	 * 获取缓存
	 *
	 * @param ids
	 * @return
	 */
	public AdminUser cacheGet(String ids) {
		AdminUser user = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + ids);
		return user;
	}

}
