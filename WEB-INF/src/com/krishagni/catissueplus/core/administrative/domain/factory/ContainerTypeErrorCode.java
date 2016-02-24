
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ContainerTypeErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	ABBREVIATION_REQUIRED,
	
	ID_OR_NAME_REQ,
	
	DUP_NAME,
	
	INVALID_CAPACITY,
	
	INVALID_LABELING_SCHEME, 
	
	HIERARCHY_CONTAINS_CYCLE;
	
	@Override
	public String code() {
		return "CONTAINER_TYPE_" + this.name();
	}
}
