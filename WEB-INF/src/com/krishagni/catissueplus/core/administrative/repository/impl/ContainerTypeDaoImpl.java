
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTypeDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ContainerTypeDaoImpl extends AbstractDao<ContainerType> implements ContainerTypeDao {
	
	@Override
	public Class<?> getType() {
		return ContainerType.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public ContainerType getByName(String name) {
		List<ContainerType> result = sessionFactory.getCurrentSession()
			.getNamedQuery(CONTAINER_TYPE_BY_NAME)
			.setParameter("name", name)
			.list();

		return result.isEmpty() ? null : result.iterator().next();
	}
	
	private static final String CONTAINER_TYPE_FQN = ContainerType.class.getName();
	
	private static final String CONTAINER_TYPE_BY_NAME = CONTAINER_TYPE_FQN + ".getByName";

}
