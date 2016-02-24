package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ContainerTypeDao extends Dao<ContainerType> {
	public List<ContainerType> getContainerTypes(ContainerTypeListCriteria crit);
	
	public ContainerType getByName(String name);
}
