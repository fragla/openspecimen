
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ContainerTypeErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	DUP_NAME,
	
	INVALID_DIMENSION_CAPACITY,
	
	INVALID_DIMENSION_LABELING_SCHEME;
	
	@Override
	public String code() {
		return "CONTAINER_TYPE_" + this.name();
	}

}
