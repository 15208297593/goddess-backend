package com.goddess.ec.manage.controller;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.model.Accessory;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.goddess.ec.manage.tools.ToolUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

/**
 * 配饰管理
 */
@Controller(controllerKey = "/admin/accessory")
public class AccessoryController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(AccessoryController.class);

    public void index() {
    	
    	setAttr("type", getPara("type"));
    	render("/accessory/accessoryList.html");
    }
        
    /**
     * 取得某个类别配饰
     * 
     */
    public void getAccessoriesByType() {
        renderJson(Db.find(ToolSqlXml.getSql("manage.accessory.getAccessoriesByType"), getPara(0)));
    }
    
    /**
     * 添加新配饰
     */
    @Before(Tx.class)
    public void saveAccessory() {
    	UploadFile f = getFile();

		Accessory m = new Accessory();
		m.set("accessory_price", getParaToInt("accessory_price"));
		m.set("accessory_type", getPara("accessory_type"));
		m.set("accessory_name", getPara("accessory_name"));
		try {
			m.save();
			if (f != null) {
				String saveURL = ToolUtils.saveUploadFile(f, AppConstants.RELATIVE_PATH_ACCESSORY, m.getInt("accessory_id").toString(), ToolUtils.getExtension(f.getOriginalFileName()));
				Accessory.dao.findById(m.getInt("accessory_id")).set("accessory_pic", saveURL).update();
			}
		} catch (IOException e) {
			e.printStackTrace();
			setAttr("errorMsg", "上传文件存储失败");
		}
		renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 更新配饰
     */
    public void updateAccessory() {
    	UploadFile upFile = getFile();
    	String accessoryId = getPara("accessory_id");
    	
		try {
			Accessory m = Accessory.dao.findById(accessoryId);
			if (upFile != null) {
				String saveURL = ToolUtils.saveUploadFile(upFile, AppConstants.RELATIVE_PATH_ACCESSORY, accessoryId, ToolUtils.getExtension(upFile.getOriginalFileName()));
				m.set("accessory_pic", saveURL);
			}
			m.set("accessory_price", getParaToInt("accessory_price"));
			m.set("accessory_name", getPara("accessory_name"));
			m.update();
		} catch (IOException e) {
			setAttr("errorMsg", "上传文件存储失败！");
			e.printStackTrace();
		}
		renderJson(new String[]{"errorMsg"});
    }
    
    
    /**
     * 删除配饰
     */
    public void deleteAccessory() {
    	Accessory.dao.deleteById(getParaToInt(0));
    	renderJson(new String[]{"errorMsg"});
    }
}
