
package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ContainerTypeDao extends Dao<ContainerType> {
	public ContainerType getByName(String name);

}
