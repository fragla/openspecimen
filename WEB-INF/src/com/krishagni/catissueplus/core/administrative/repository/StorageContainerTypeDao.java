
package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerType;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface StorageContainerTypeDao extends Dao<StorageContainerType> {
	public StorageContainerType getByName(String name);

}
