package com.goddess.ec.manage.controller;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.common.DictKeys;
import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.model.LogoPrint;
import com.goddess.ec.manage.model.LogoType;
import com.goddess.ec.manage.plugin.PropertiesPlugin;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.goddess.ec.manage.tools.ToolUtils;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;

/**
 * logo印制样式管理
 */
@Controller(controllerKey = "/admin/logoPrint")
public class LogoPrintController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(LogoPrintController.class);

    public void index() {
    	
    	setAttr("logoTypes", LogoType.toJson());
    	render("/logoPrint/logoPrintList.html");
    }
        
    /**
     * 取得所有logo印制样式
     * 
     */
    public void getLogoPrints() {
        renderJson(Db.find(ToolSqlXml.getSql("manage.logoPrint.getLogoPrints")));
    }
    
    /**
     * 添加新logo印制样式
     */
    @Before(Tx.class)
    public void saveLogoPrint() {
    	UploadFile f = getFile();

		LogoPrint m = new LogoPrint();
		m.set("logo_name", getPara("logo_name"));
		
		LogoType lt = LogoType.getEnumByCode(getPara("logo_type"));
		m.set("logo_type", lt.getCode());
		m.set("logo_type_name", lt.getText());
		m.set("is_multi", lt.isMulti());
		m.set("logo_price", PropertiesPlugin.getParamMapValue(DictKeys.logo_print_price));
		try {
			m.save();
			if (f != null) {
				String saveURL = ToolUtils.saveUploadFile(f, AppConstants.RELATIVE_PATH_LOGO_PRINT, m.getInt("logo_print_id").toString(), ToolUtils.getExtension(f.getOriginalFileName()));
				LogoPrint.dao.findById(m.getInt("logo_print_id")).set("logo_pic", saveURL).update();
			}
		} catch (IOException e) {
			e.printStackTrace();
			setAttr("errorMsg", "上传文件存储失败");
		}
		renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 更新logo印制样式
     */
    public void updateLogoPrint() {
    	UploadFile upFile = getFile();
    	String logoPrintId = getPara("logo_print_id");
    	
		try {
			LogoPrint m = LogoPrint.dao.findById(logoPrintId);
			m.set("logo_name", getPara("logo_name"));
			
			LogoType lt = LogoType.getEnumByCode(getPara("logo_type"));
			m.set("logo_type", lt.getCode());
			m.set("logo_type_name", lt.getText());
			m.set("is_multi", lt.isMulti());
			m.set("logo_price", PropertiesPlugin.getParamMapValue(DictKeys.logo_print_price));
			if (upFile != null) {
				String saveURL = ToolUtils.saveUploadFile(upFile, AppConstants.RELATIVE_PATH_LOGO_PRINT, logoPrintId, ToolUtils.getExtension(upFile.getOriginalFileName()));
				m.set("logo_pic", saveURL);
			}
			m.update();
		} catch (IOException e) {
			setAttr("errorMsg", "上传文件存储失败！");
			e.printStackTrace();
		}
		renderJson(new String[]{"errorMsg"});
    }
    
    
    /**
     * 删除logo印制样式
     */
    public void deleteLogoPrint() {
    	LogoPrint.dao.deleteById(getParaToInt(0));
    	renderJson(new String[]{"errorMsg"});
    }
}
