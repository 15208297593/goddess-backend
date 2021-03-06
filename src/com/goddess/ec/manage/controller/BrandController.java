package com.goddess.ec.manage.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Controller;
import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.constants.AppConstants;
import com.goddess.ec.manage.model.Brand;
import com.goddess.ec.manage.service.BrandService;
import com.goddess.ec.manage.tools.ToolSqlXml;
import com.goddess.ec.manage.tools.ToolUtils;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.upload.UploadFile;

/**
 * 品牌管理
 */
@Controller(controllerKey = "/admin/brand")
public class BrandController extends BaseController {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(BrandController.class);

    public void index() {
    	render("/brand/brandList.html");
    }
    
    public void getBrand() {

    	int brandId = getParaToInt(0);
    	Brand b = Brand.dao.findById(brandId);
    	setAttr("b", b);
    	render("/brand/brandDetail.html");
    }
    
    public void initBrand() {

    	setAttr("b", new Brand());
    	render("/brand/brandDetail.html");
    }
    
    /**
     * 品牌类别列表
     */
    public void listBrands() {
        SplitPage sp = new SplitPage();
        // 检索条件设定
        sp.setPageNumber(getParaToInt("page"));
        sp.setPageSize(getParaToInt("rows"));
        sp.setOrderColunm(getPara("sort"));
        sp.setOrderMode(getPara("order"));
        
        Map<String, String> params = new HashMap<String, String>();
        sp.setQueryParam(params);

        BrandService.service.getBrands(sp);

        Map<String, Object> jsonRes = new HashMap<String, Object>();
		jsonRes.put("total",  sp.getPage().getTotalRow());
		jsonRes.put("rows",  sp.getPage().getList());
        renderJson(jsonRes);
    }
    
    /**
     * 同步所有品牌新品数量
     */
    public void syncAllNewAmount() {
    	Db.update(ToolSqlXml.getSql("manage.brand.syncAllNewAmount"), AppConstants.IS_NEW_YES);
    	renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 同步新品数量
     */
    public void syncNewAmount() {
    	int brandId = getParaToInt(0);
    	Db.update(ToolSqlXml.getSql("manage.brand.syncNewAmount"), AppConstants.IS_NEW_YES, brandId, brandId);
    	renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 添加新品牌
     */
    public void saveBrand() {
    	
    	List<UploadFile> upFiles = getFiles();
    	Brand b = createBrand();
    	// 保存新类别
    	if (!b.save()) {
    		setAttr("errorMsg", "品牌添加失败！");
    	} else {
    		try {
    			if (upFiles != null && upFiles.size() != 0) {
    				b = new Brand().set("brand_id", b.getInt("brand_id"));
    				uploadFile(b, upFiles);
    				Brand.dao.findById(b.getInt("brand_id")).setAttrs(b).update();
    			}
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    			setAttr("errorMsg", "上传文件存储失败!");
    		}
    		setAttr("brand",b);
    	}
    	renderJson(new String[]{"errorMsg", "brand"});
    }
    
    private void uploadFile(Brand b, List<UploadFile> upFiles) throws IOException {

		int brandId = b.getInt("brand_id");
		for (UploadFile f : upFiles) {
			String picType = f.getParameterName().replace("_file", "");
			String saveURL = ToolUtils.saveUploadFile(f, AppConstants.RELATIVE_PATH_BRAND + brandId, 
					picType, ToolUtils.getExtension(f.getOriginalFileName()));
			b.set(picType, saveURL);
		}
    }
    
    private Brand createBrand() {

    	return new Brand().set(
				"brand_name", getPara("brand_name")).set(
				"brand_des", getPara("brand_des")).set(
				"brand_initial", getPara("brand_initial")).set(
				"delete_flag", AppConstants.DELETE_FLAG_VALID);
    }
    
    /**
     * 验证是否可更新品牌，该品牌商品在购物车，未付款订单；该品牌基础款正在被用户定制，未付款{brand_id}
     */
    public void checkUpdateBrand() {
    	checkUpdate("checkBrandIsUsed", getPara(0), "该品牌商品");
    }
    
    private void checkUpdate(String sqlId, String ids, String msg) {
//    	List<Record> res = Db.find(ToolSqlXml.getSql("manage.brand."+sqlId), 
//    			AppConstants.CUSTOMIZE_STATUS_FINISHED, ids, AppConstants.IS_SHELVED_ON, ids);
//    	if (res.size() == 2) {
//    		String errorMsg = "%s正在被用户【%s】用于未完成的定制中，并且正在被可定制上架商品【%s】使用中";
//    		String cellNum = res.get(0).getStr("cell_num");
//    		Integer commodityId = res.get(0).getInt("commodity_id");
//    		if (cellNum == null)
//    			cellNum = res.get(1).getStr("cell_num");
//    		else if (commodityId == null)
//    			commodityId = res.get(1).getInt("commodity_id");
//    		setAttr("errorMsg", String.format(errorMsg, msg, cellNum, commodityId));
//    	} else if (res.size() == 1) {
//    		if (StringUtils.isNotEmpty(res.get(0).getStr("cell_num")))
//    			setAttr("errorMsg", String.format("%s正在被用户【%s】用于未完成的定制中", msg, res.get(0).getStr("cell_num")));
//    		else
//    			setAttr("errorMsg", String.format("%s正在被可定制上架商品【%s】使用中", msg, res.get(0).getInt("commodity_id")));
//    	}
    	renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 更新品牌
     */
    public void updateBrand() {

    	List<UploadFile> upFiles = getFiles();
    	Brand b = createBrand();
    	b.set("brand_id", getParaToInt("brand_id"));
		try {
			if (upFiles != null && upFiles.size() != 0) {
				uploadFile(b, upFiles);
			}
		} catch (IOException e) {
			e.printStackTrace();
			setAttr("errorMsg", "上传文件存储失败");
		}
		Brand.dao.findById(b.getInt("brand_id")).setAttrs(b).update();
    	renderJson(new String[]{"errorMsg"});
    }
    
    /**
     * 删除品牌
     */
    public void deleteBrand() {
    	// TODO 是否下架该品牌相关所有商品，删除该品牌对应的基础款？
    	if (!Brand.dao.findById(getPara(0)).set("delete_flag", AppConstants.DELETE_FLAG_INVALID).update())
    		setAttr("errorMsg", "删除失败");
    	renderJson(new String[]{"errorMsg"});
    }
}
