package com.goddess.ec.manage.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * 系统参数管理
 */
@Controller(controllerKey = "/admin/sysparams")
public class SysParamsController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(SysParamsController.class);

    public void index() {
    	
    	render("/sysparams/paramsList.html");
    }
        
    /**
     * 取得某个类别系统参数
     * 
     */
    public void getSysparams() {
    	
    	String category = getPara(0);
    	if ("highend_tags".equals(category)) {
    		Record r = Db.findFirst(ToolSqlXml.getSql("manage.sysparams.getSystemParams"), getPara(0));
    		String[] tags = r.getStr("text").split(",");
    		List<Record> sysparams = new ArrayList<Record>();
    		for (String tag : tags) {
    			sysparams.add(new Record().set("category", r.get("category")).set("text", tag));
    		}
    		renderJson(sysparams);
    	} else {
    		renderJson(Db.find(ToolSqlXml.getSql("manage.sysparams.getSystemParams"), category));
    	}
    }
    
    /**
     * 添加新系统参数
     */
    @Before(Tx.class)
    public void saveSysparams() {

		if (getPara("code", null) == null)
    		Db.update(ToolSqlXml.getSql("manage.sysparams.deleteByCategory"), getPara("category"));
		Db.update(ToolSqlXml.getSql("manage.sysparams.insertSystemParams"), getPara("category"), getPara("code", null), getPara("text", null));
		renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 更新系统参数
     */
    public void updateSysparams() {
    	
    	Db.update(ToolSqlXml.getSql("manage.sysparams.updateSystemParams"), getPara("code", null), getPara("text", null), getPara("category"));
		renderJson(new String[]{"errorMsg"});
    }
    
    
    /**
     * 删除系统参数
     */
    public void deleteSysparams() {
    	if (getPara("code", null) == null)
        	Db.update(ToolSqlXml.getSql("manage.sysparams.updateSystemParams"), null, getPara("text", null), getPara("category"));
    	else
    		Db.update(ToolSqlXml.getSql("manage.sysparams.deleteSystemParams"), getPara("category"), getPara("code", null));
    	renderJson(new String[]{"errorMsg"});
    }
}
