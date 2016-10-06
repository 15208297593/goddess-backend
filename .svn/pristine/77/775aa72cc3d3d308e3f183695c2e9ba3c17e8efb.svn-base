package com.goddess.ec.manage.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.model.Catalog;
import com.goddess.ec.manage.tools.ToolSqlFormatter;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

@Controller(controllerKey = "/admin/catalog")
public class CatalogController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(CatalogController.class);

    /**
     * 订单列表
     */
    public void index() {
    	render("/catalog/catalogTree.html");
    }
    
    public void catalogTree() {

    	List<Record> allCatalog = Db.find(ToolSqlXml.getSql("manage.catalog.getAllCatalog"));
    	Map<Integer, Record> nodeMap = new HashMap<Integer, Record>();
    	List<Record> catalogTree = new ArrayList<Record>();
    	for (Record r : allCatalog) {
    		Integer parentId = r.getInt("parent_id");

			nodeMap.put(r.getInt("id"), r);
    		if (parentId != 0) {

    			Record node = nodeMap.get(parentId);
    			List<Record> children = node.get("children");
    			if (children == null) {
    				children = new ArrayList<Record>();
    				node.set("children", children);
    			}
    			children.add(r);
    		// 0级结点，父结点为0
    		} else {
    			catalogTree.add(r);
    		}
    	}

        renderJson(catalogTree);
    }
    
    public void saveCatalog() {
    	Catalog ca = new Catalog();
    	ca.set("catalog_name", getPara("name")).set(
    			"rank", getParaToInt("rank")).set(
    			"catalog_level", getParaToInt("level")).set(
    			"parent_id", getParaToInt("parent_id"));
    	
    	ca.save();
    	renderJson(new String[]{});
    }
    
    public void updateCatalog() {

    	Catalog ca = Catalog.dao.findById(getParaToInt("id"));
    	ca.set("catalog_name", getPara("name")).set(
    			"rank", getParaToInt("rank"));
    	ca.update();
    	renderJson(new String[]{});
    }
    
    /**
     * 删除该目录及其子目录（id1_id2_id3...）
     */
    @Before(Tx.class)
    public void deleteCatalog() {
    	
    	// 删除该目录及其子目录
    	String[] ids = getPara(0).split("_");
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("ids", ToolSqlFormatter.formatInPart(ids.length));
    	Db.update(ToolSqlXml.getSql("manage.catalog.deleteCatalogs", params), ids);
    	
    	// 删除商品目录
    	Db.update(ToolSqlXml.getSql("manage.catalog.deleteCommodityCatalogs", params), ids);

    	renderJson(new String[]{});
    }
}
