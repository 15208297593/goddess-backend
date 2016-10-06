package com.goddess.ec.manage.model;

import org.apache.log4j.Logger;

import com.goddess.ec.manage.annotation.Table;

@Table(tableName = "classification", pkName = "classification_id")
public class Classification extends BaseModel<Classification> {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7726574874962652280L;

	@SuppressWarnings("unused")
    private static Logger log = Logger.getLogger(Classification.class);

    public static final Classification dao = new Classification();

}
