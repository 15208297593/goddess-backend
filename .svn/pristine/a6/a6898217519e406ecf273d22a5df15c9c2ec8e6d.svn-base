package com.goddess.ec.manage.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.model.Scene;
import com.goddess.ec.manage.model.User;
import com.goddess.ec.manage.tools.ToolSqlXml;

/**
 * 系统初始化缓存操作类
 */
public class ThreadParamInit extends Thread {

	private static Logger log = Logger.getLogger(ThreadParamInit.class);

	public static String cacheStart_user = "user_";
	
    public static String cacheStart_scene = "scene_";

	@Override
	public void run() {
		log.info("缓存参数初始化 start ...");

		// 1.缓存用户
//		cacheUser();

        // 缓存场景
//		cacheScenes();
		log.info("缓存参数初始化 end ...");
	}

	/**
	 * 缓存所有用户
	 */
	public static void cacheUser() {
		// 初期缓存对象选取
		String sql = ToolSqlXml.getSql("manage.user.all");
		List<User> userList = User.dao.find(sql);
		User.dao.cacheAddAll(userList);
//		for (User user : userList) {
//			User.dao.cacheAdd(user.getStr("IDS"));
//			user = null;
//		}
		userList = null;
	}

    /**
     * 缓存场景
     */
    public static void cacheScenes() {
        String sql = ToolSqlXml.getSql("manage.scene.allType");
        List<Scene> sceneList = Scene.dao.find(sql);
        for (Scene scene : sceneList) {
        	Scene.dao.cacheAdd(scene.getStr("scene_type"));
            scene = null;
        }
        sceneList = null;
    }
}
