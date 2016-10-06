package com.goddess.ec.manage.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.model.Manufacturer;

/**
 * 生产商管理
 */
@Controller(controllerKey = "/admin/manufacturer")
public class ManufacturerController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(ManufacturerController.class);

    public void index() {
    	render("/manufacturer/manufacturerList.html");
    }
    
    /**
     * 生产商列表
     */
    public void getManufacturers() {

        Map<String, Object> jsonRes = new HashMap<String, Object>();
        List<Manufacturer> mList = Manufacturer.dao.find("select * from manufacturer");
        jsonRes.put("total", mList.size());
        jsonRes.put("rows",  mList);
        
        renderJson(jsonRes);
    }
    
    /**
     * 新增加生产商
     */
    public void saveManufacturer() {
    	
    	Manufacturer m = new Manufacturer();
    	if (!m.set("manufacturer_name", getPara("manufacturer_name")).set(
    				"manufacturer_email", getPara("manufacturer_email")).set(
					"manufacture_type", getPara("manufacture_type")).save()) {
    		setAttr("errorMsg", "生产商添加失败！");
    	} else {
    		setAttr("manufacturer",m);
    	}
    	renderJson(new String[]{"errorMsg", "manufacturer"});
    }
    
    /**
     * 更新生产商
     */
    public void updateManufacturer() {
    	
    	// TODO 可更新验证，用户是否正在使用该生产商
    	Manufacturer m = Manufacturer.dao.findById(getPara("manufacturer_id"));
    	// 保存新类别
    	if (!m.set("manufacturer_name", getPara("manufacturer_name")).set(
				"manufacturer_email", getPara("manufacturer_email")).set(
				"manufacture_type", getPara("manufacture_type")).update()) {
    		setAttr("errorMsg", "生产商更新失败！");
    	}
    	setAttr("manufacturer",m);
    	renderJson(new String[]{"errorMsg", "manufacturer"});
    }
    
    /**
     * 删除生产商
     */
    public void deleteManufacturer() {
    	// TODO 可更新验证，用户是否正在使用该生产商
    	if (!Manufacturer.dao.deleteById(getPara(0)))
    		setAttr("errorMsg", "删除失败");
    	renderJson(new String[]{"errorMsg"});
    }
}
