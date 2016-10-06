package com.goddess.ec.manage.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.model.Material;
import com.goddess.ec.manage.model.MaterialType;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.plugin.activerecord.Db;

public class MaterialService  extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(MaterialService.class);

    public static final MaterialService service = new MaterialService();

    /**
     * 分页显示所有原材料类别
     * @param sp
     */
    public void getMaterialTypes(SplitPage sp) {
    	splitPageBase(sp, "select *", "manage.material.allTypes");
    }
    
    public List<MaterialType> getMaterialTypes() {
    	return MaterialType.dao.find("select * from material_type");
    }
    
    /**
     * 根据原材料类别取得原材料
     * @param typeId
     * @return
     */
    public List<Material> getMaterialsByType(String typeId) {
    	return Material.dao.find(ToolSqlXml.getSql("manage.material.getMaterialsByType"), typeId);
    }
    
    public void deleteCustomizationByType(String materialTypeId) {
    	Db.update(ToolSqlXml.getSql("manage.material.deleteCustomizationByType"), materialTypeId);
    }
    public void deleteCustomizationByProperty(String materialTypeId, String materialId) {
    	Db.update(ToolSqlXml.getSql("manage.material.deleteCustomizationByProperty"), materialTypeId, materialId);
    }
}
