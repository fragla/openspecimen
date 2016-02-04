package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerType;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerTypeDetail;

public interface StorageContainerTypeFactory {
	public StorageContainerType createStorageContainerType(StorageContainerTypeDetail detail, StorageContainerType canHold);
	
}
