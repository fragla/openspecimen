
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerType;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerTypeDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class StorageContainerTypeDaoImpl extends AbstractDao<StorageContainerType> implements StorageContainerTypeDao {
	
	@Override
	@SuppressWarnings("unchecked")
	public StorageContainerType getByName(String name) {
		List<StorageContainerType> result = sessionFactory.getCurrentSession()
				.getNamedQuery(STORAGE_CONTAINER_TYPES_BY_NAME)
				.setParameter("name", name)
				.list();

		return result.isEmpty() ? null : result.iterator().next();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public StorageContainerType getById(Long id) {
		List<StorageContainerType> result = sessionFactory.getCurrentSession()
				.getNamedQuery(STORAGE_CONTAINER_TYPES_BY_ID)
				.setParameter("id", id)
				.list();

		return result.isEmpty() ? null : result.iterator().next();
	}

	private static final String STORAGE_CONTAINER_TYPES_FQN = StorageContainerType.class.getName();
	
	private static final String STORAGE_CONTAINER_TYPES_BY_NAME = STORAGE_CONTAINER_TYPES_FQN + ".getStorageContainerTypesByName";
	
	private static final String STORAGE_CONTAINER_TYPES_BY_ID = STORAGE_CONTAINER_TYPES_FQN + ".getStorageContainerTypesById";
	
	
}
