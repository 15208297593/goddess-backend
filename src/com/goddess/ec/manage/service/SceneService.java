package com.goddess.ec.manage.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Element;

import com.alibaba.druid.support.json.JSONUtils;
import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.model.Scene;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class SceneService  extends BaseService {

    private static Logger log = Logger.getLogger(SceneService.class);

    public static final SceneService service = new SceneService();


	public static Map<String, Object> sceneTree = new HashMap<String, Object>();

	public static Object treeJson = null;
	
	static {
		
		try {
			String sceneTreeJson = FileUtils.readFileToString(
					FileUtils.toFile(ToolSqlXml.class.getClassLoader().getResource("scene_tree.json")),"UTF-8");
			treeJson = JSONUtils.parse(sceneTreeJson); 
		} catch (IOException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(treeJson);
	}

	@SuppressWarnings("unchecked")
	private static void parseNode(Element node, Map<String, Object> parent) {

		for (Iterator<Attribute> iterTemp = node.attributeIterator(); iterTemp.hasNext();) {
			Attribute attr = (Attribute) iterTemp.next();
			parent.put(attr.getName(), attr.getValue());
		}
		int nodeCount = node.nodeCount();
		if (nodeCount == 0) return;
		
		Map<String, Object> children = new HashMap<String, Object>();
		parent.put(node.attributeValue("code"), children);
		for (Iterator<Element> iterTemp = node.elementIterator(); iterTemp.hasNext();) {
			Element element = (Element) iterTemp.next();
			parseNode(element, children);
		}
	}
	
    /**
     * 取得所有场景
     * @return
     */
    public List<Scene> getScenes(Map<String, Object> params) {
    	
    	return Scene.dao.find(ToolSqlXml.getSql("manage.scene.getScenes", params));
    }
    
    /**
     * 取得所有上架商品及关联商品
     * @param sceneCommodities
     * @return
     */
	public List<Record> getRelatedCommodities(Integer sceneId, Integer selectedId) {

    	List<Record> commodities = Db.find(ToolSqlXml.getSql("manage.scene.getRelatedCommodities"), selectedId, sceneId, AppConstants.IS_SHELVED_ON);
//    	if (StringUtils.isNotBlank(sceneCommodities)) {
//	    	Map sc = (Map)JSONUtils.parse(sceneCommodities);
//	    	for (Record r : commodities) {
//	    		if (sc.containsKey(r.getInt("commodity_id").toString())) {
//	    			r.set("is_selected", '1');
//	    		}
//	    	}
//    	}
    	return commodities;
    }

    /**
     * 取得所有品牌
     * @return
     */
    public List<Record> getRelatedBrands() {
    	
    	List<Record> brands = Db.find(ToolSqlXml.getSql("manage.scene.getRelatedBrands"), AppConstants.DELETE_FLAG_VALID);
    	return brands;
    }
    
    /**
     * 保存场景商品列表
     * @param commodities
     * @param sceneId
     */
    public void saveSceneCommodities(String[] commodities, Integer sceneId) {

		List<Record> sceneComms = new ArrayList<Record>();
		for (String commId : commodities) {
			sceneComms.add(new Record().set("scene_id", sceneId).set("commodity_id", Integer.valueOf(commId)));
		}
		Db.batch("insert into scene_commodities(scene_id, commodity_id) values(?,?)", "scene_id, commodity_id", sceneComms, sceneComms.size());
    }
    
    public void deleteSceneCommodities(Integer sceneId) {
    	Db.update("delete from scene_commodities where scene_id = ?", sceneId);
    }
}
