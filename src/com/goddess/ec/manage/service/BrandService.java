package com.goddess.ec.manage.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.common.SplitPage;
import com.goddess.ec.manage.model.Brand;
import com.goddess.ec.manage.tools.ToolSqlXml;

public class BrandService extends BaseService {

    @SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(BrandService.class);

    public static final BrandService service = new BrandService();

    /**
     * 分页显示品牌
     * @param sp
     */
    public void getBrands(SplitPage sp) {
    	splitPageBase(sp, "select * ", "manage.brand.all");
    }
    
    public List<Brand> getAll() {
    	return Brand.dao.find(ToolSqlXml.getSql("manage.brand.allBrands"));
    }
}
