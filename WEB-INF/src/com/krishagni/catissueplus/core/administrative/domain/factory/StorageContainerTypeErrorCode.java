
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum StorageContainerTypeErrorCode implements ErrorCode {
	ID_NOT_FOUND,
	
	NAME_NOT_FOUND,
	
	NAME_REQUIRED,
	
	ID_OR_NAME_REQ,
	
	DUP_NAME,
	
	INVALID_DIMENSION_CAPACITY,
	
	INVALID_DIMENSION_LABELING_SCHEME;
	
	@Override
	public String code() {
		return "STORAGE_CONTAINER_TYPE_" + this.name();
	}

}
