
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerType;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerTypeDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class StorageContainerTypeDaoImpl extends AbstractDao<StorageContainerType> implements StorageContainerTypeDao {
	
	@Override
	public Class<?> getType() {
		return StorageContainerType.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public StorageContainerType getByName(String name) {
		List<StorageContainerType> result = sessionFactory.getCurrentSession()
				.getNamedQuery(STORAGE_CONTAINER_TYPES_BY_NAME)
				.setParameter("name", name)
				.list();

		return result.isEmpty() ? null : result.iterator().next();
	}
	
	private static final String STORAGE_CONTAINER_TYPES_FQN = StorageContainerType.class.getName();
	
	private static final String STORAGE_CONTAINER_TYPES_BY_NAME = STORAGE_CONTAINER_TYPES_FQN + ".getStorageContainerTypesByName";

}
