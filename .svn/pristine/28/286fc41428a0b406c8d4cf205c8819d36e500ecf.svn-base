package com.goddess.ec.manage.model;

import java.util.List;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;
import com.goddess.ec.manage.common.DictKeys;
import com.goddess.ec.manage.thread.ThreadParamInit;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.plugin.ehcache.CacheKit;

@Table(tableName = "scene", pkName="scene_id")
public class Scene extends BaseModel<Scene> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1899068893404855658L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(Scene.class);

    public static final Scene dao = new Scene();

	/**
	 * 添加或者更新缓存
	 */
	public void cacheAdd(String sceneType) {
		List<Scene> scenes = Scene.dao.find(ToolSqlXml.getSql("manage.scene.getSceneByType"), sceneType);
		CacheKit.put(DictKeys.cache_name_system, ThreadParamInit.cacheStart_scene + sceneType, scenes);
	}

	/**
	 * 删除缓存
	 *
	 * @param sceneType
	 */
	public void cacheRemove(String sceneType) {
		CacheKit.remove(DictKeys.cache_name_system, ThreadParamInit.cacheStart_scene + sceneType);
	}

	/**
	 * 获取缓存
	 *
	 * @param sceneType
	 * @return
	 */
	public List<Scene> cacheGet(String sceneType) {
		List<Scene> scenes = CacheKit.get(DictKeys.cache_name_system, ThreadParamInit.cacheStart_scene + sceneType);
		return scenes;
	}
}
