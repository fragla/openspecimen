package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ContainerTypeListCriteria extends AbstractListCriteria<ContainerTypeListCriteria> {

	private Long canHold;

	@Override
	public ContainerTypeListCriteria self() {
		return this;
	}

	public Long canHold() {
		return canHold;
	}

	public ContainerTypeListCriteria canHold(Long canHold) {
		this.canHold = canHold;
		return self();
	}
	
}
