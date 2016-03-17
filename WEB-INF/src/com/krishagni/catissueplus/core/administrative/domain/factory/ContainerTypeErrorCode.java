
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ContainerTypeErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	ID_OR_NAME_REQ,
	
	DUP_NAME,
	
	INVALID_CAPACITY,
	
	INVALID_LABELING_SCHEME, 
	
	REF_ENTITY_FOUND,

	CYCLES_NOT_ALLOWED,

	CONTAINER_NAME_FMT_REQUIRED,

	INVALID_CONTAINER_NAME_FMT;
	
	@Override
	public String code() {
		return "CONTAINER_TYPE_" + this.name();
	}
}
