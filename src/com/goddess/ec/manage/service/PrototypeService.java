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

public class PrototypeService extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(PrototypeService.class);

    public static final PrototypeService service = new PrototypeService();

    /**
     * 分页显示原型
     * @param sp
     */
    public void getPrototypes(SplitPage sp) {
    	splitPageBase(sp, "select * ", "manage.prototype.all");
    }
    
    public List<Record> getAllPrototypes() {
    	return Db.find(ToolSqlXml.getSql("manage.prototype.getAllPrototypes"));
    }
    
    public List<Record> getPrototypeAccessory(int prototypeId) {
    	return Db.find(ToolSqlXml.getSql("manage.accessory.getPrototypeAccessory"), prototypeId);
    }
    
    public List<Record> getPrototypePics(int prototypeId) {
    	return Db.find(ToolSqlXml.getSql("manage.prototype.getPrototypePics"), prototypeId);
    }

    public List<Record> getMaterialTypes() {
    	return Db.find(ToolSqlXml.getSql("manage.prototype.getMaterialTypes"));
    }
    
    /**
     * 
     * @param prototypeId
     */
    public List<Record> getPropertyTypes(int prototypeId) {
    	List<Record> records = Db.find(ToolSqlXml.getSql("manage.prototype.getPropertyTypes"), prototypeId);
    	return records;
    }
    
    public String getTypeRank(int prototypeId) {
    	Record rank = Db.findFirst(ToolSqlXml.getSql("manage.prototype.getTypeRank"), prototypeId);
    	if (rank != null) return rank.getStr("rank");
    	return null;
    }
    
    /**
     * 
     * @param prototypeId
     */
    public List<Record> getProperties(int materialTypeId, int propertyTypeId) {

    	if (propertyTypeId == 0) {
    		return Db.find(ToolSqlXml.getSql("manage.prototype.getProperties"), materialTypeId);
    	} else {
    		return Db.find(ToolSqlXml.getSql("manage.prototype.getPropertiesWithTypeId"), propertyTypeId, materialTypeId);
    	}
    }
    
    /**
     * 删除该类别的所有子属性类别及其属性
     * @param parentTypeId
     * @return
     */
    public boolean deleteChildrenType(int parentTypeId) {
		return Db.update("delete pt,p from property_type pt, properties p where pt.property_type_id = p.property_type_id and pt.parent_type_id = ?", parentTypeId) != 0;
    }
    
    public int[] updateTypeRank(List<Record> typeRank) {
    	return Db.batch(ToolSqlXml.getSql("manage.prototype.updateTypeRank"), "rank, property_type_id", typeRank, typeRank.size());
    }
    
    public void deletePrototypeAccByType(int prototypeId, String accessoryType) {
    	Db.update(ToolSqlXml.getSql("manage.prototype.deletePrototypeAccByType"), prototypeId, accessoryType);
    }
    
    public List<Record> getBaseRelateTypes(Integer baseType, Integer relateType, String[] pids) {

    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("base_type", baseType);
    	params.put("relate_type", relateType);
    	params.put("pids", ToolSqlFormatter.formatInPart(pids.length));
    	return Db.find(ToolSqlXml.getSql("manage.prototype.getBaseRelateTypes", params), pids);
    }
    
    public void deleteBaseRelateTypes(Integer baseType, Integer relateType, String[] pids) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("base_type", baseType);
    	params.put("relate_type", relateType);
    	params.put("pids", ToolSqlFormatter.formatInPart(pids.length));
    	Db.update(ToolSqlXml.getSql("manage.prototype.deleteBaseRelateTypes", params), pids);
    }
    
    public void deleteRelateType(Integer baseType, Integer relateType) {
    	
    	Map<String, Object> params = new HashMap<String, Object>();
    	params.put("base_type", baseType);
    	params.put("relate_type", relateType);
    	Db.update(ToolSqlXml.getSql("manage.prototype.deleteRelateType", params));
    }
}
