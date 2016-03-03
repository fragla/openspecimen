
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTypeDao;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTypeListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ContainerTypeDaoImpl extends AbstractDao<ContainerType> implements ContainerTypeDao {
	
	@Override
	public Class<?> getType() {
		return ContainerType.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContainerType> getContainerTypes(ContainerTypeListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(ContainerType.class)
			.setFirstResult(crit.startAt())
			.setMaxResults(crit.maxResults())
			.addOrder(Order.asc("name"));;
		
		addSearchConditions(query, crit);
		return query.list();
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
	
	private void addSearchConditions(Criteria query, ContainerTypeListCriteria crit) {
		if (StringUtils.isBlank(crit.query())) {
			return;
		}
		
		MatchMode matchMode = crit.exactMatch() ? MatchMode.EXACT : MatchMode.ANYWHERE;
		query.add(Restrictions.ilike("name", crit.query(), matchMode));
	}
	
	private static final String CONTAINER_TYPE_FQN = ContainerType.class.getName();
	
	private static final String CONTAINER_TYPE_BY_NAME = CONTAINER_TYPE_FQN + ".getByName";
}
