package com.goddess.ec.manage.model;

import java.util.List;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;
import com.goddess.ec.manage.common.DictKeys;
import com.goddess.ec.manage.thread.ThreadParamInit;
import com.jfinal.plugin.ehcache.CacheKit;

@Table(tableName = "users", pkName="user_id")
public class User extends BaseModel<User> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4228707163498175070L;

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(User.class);

	public static final User dao = new User();

	/**
	 * 添加或者更新缓存
	 */
	public void cacheAdd(String ids) {
		User user = User.dao.findById(ids);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + ids, user);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("cell_num"), user);
	}

	/**
	 * 添加多个用户到缓存中
	 * @param allUsers
	 */
	public void cacheAddAll(List<User> allUsers) {
		for (User user : allUsers) {
			CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getInt("user_id"), user);
			CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("cell_num"), user);
		}
	}

	/**
	 * 删除缓存
	 */
	public void cacheRemove(String ids) {
		User user = User.dao.findById(ids);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + ids);
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + user.getStr("cell_num"));
	}

	/**
	 * 获取缓存
	 *
	 * @param ids
	 * @return
	 */
	public User cacheGet(String ids) {
		User user = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_user + ids);
		return user;
	}

}
