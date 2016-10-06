package com.goddess.ec.manage.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.tools.ToolSqlFormatter;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

public class PropertyService extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(PropertyService.class);

    public static final PropertyService service = new PropertyService();

    /**
     * 分页显示属性类别
     * @param sp
     */
    public void getPropertyTypes(SplitPage sp) {
    	splitPageBase(sp, "select pt.*,mt.type_name as m_type_name ", "manage.property.refPropertyTypes");
    }
    
    public int[] savePropertiesWithRank(List<Record> properties) {
    	return Db.batch(ToolSqlXml.getSql("manage.property.savePropertiesWithRank"), "property_type_id, material_id, rank", properties, properties.size());
    }
    
    public int[] saveProperties(List<Record> properties) {
		return Db.batch(ToolSqlXml.getSql("manage.property.saveProperty"), "property_type_id, material_id", properties, properties.size());
    }
    
    public int deleteProperties(List<String> properties) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("properties", ToolSqlFormatter.formatInPart(properties.size()));
    	return Db.update(ToolSqlXml.getSql("manage.property.deleteProperty", params), properties.toArray());
    }

    public void deleteBaseRelateProperties(List<String> properties) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("properties", ToolSqlFormatter.formatInPart(properties.size()));
    	Db.update(ToolSqlXml.getSql("manage.property.deleteBaseRelateProperties", params), properties.toArray());
    }
    
    public void deleteBaseRelatePropertiesByType(int propertyTypeId) {
    	Db.update(ToolSqlXml.getSql("manage.property.deleteBaseRelatePropertiesByType"), propertyTypeId);
    }
    
    public boolean deletePropertiesByType(int propertyTypeId) {
    	return Db.update(ToolSqlXml.getSql("manage.property.deletePropertiesByType"), propertyTypeId) != 0;
    }
    
    public int[] updatePropertyRank(List<Record> propertyRank) {
    	return Db.batch(ToolSqlXml.getSql("manage.property.updatePropertyRank"), "rank, property_id", propertyRank, propertyRank.size());
    }

    public String getPropertyRank(int propertyTypeId) {
    	Record rank = Db.findFirst(ToolSqlXml.getSql("manage.prototype.getPropertyRank"), propertyTypeId);
    	if (rank != null) return rank.getStr("rank");
    	return null;
    }
}
