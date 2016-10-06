package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "prototype_pic", pkName = "prototype_pic_id")
public class PrototypePic extends BaseModel<PrototypePic> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7561613829109407664L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(PrototypePic.class);

    public static final PrototypePic dao = new PrototypePic();

}
