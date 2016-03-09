
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
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
			.addOrder(Order.asc("name"));
		
		addSearchConditions(query, crit);
		return query.list();
	}
	
	@Override
	public int getContainerTypesCount(ContainerTypeListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(ContainerType.class)
			.setProjection(Projections.rowCount());
		
		addSearchConditions(query, crit);
		return ((Number)query.uniqueResult()).intValue();
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
		MatchMode matchMode = crit.exactMatch() ? MatchMode.EXACT : MatchMode.ANYWHERE;
		addNameRestriction(query, crit.query(), matchMode);
		addCanHoldRestriction(query, crit.canHold());
	}
	
	private void addNameRestriction(Criteria query, String name, MatchMode matchMode) {
		if (StringUtils.isBlank(name)) {
			return;
		}
		
		query.add(Restrictions.ilike("name", name, matchMode));
	}
	
	private void addCanHoldRestriction(Criteria query, Long canHoldId) {
		if (canHoldId == null) {
			return;
		}
		
		query.add(Restrictions.eq("canHold.id", canHoldId));
	}
	
	private static final String CONTAINER_TYPE_FQN = ContainerType.class.getName();
	
	private static final String CONTAINER_TYPE_BY_NAME = CONTAINER_TYPE_FQN + ".getByName";
}
