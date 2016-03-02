package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ContainerTypeListCriteria extends AbstractListCriteria<ContainerTypeListCriteria> {

	Long canHold;
	
	public Long getCanHold() {
		return canHold;
	}

	public void setCanHold(Long canHold) {
		this.canHold = canHold;
	}

	@Override
	public ContainerTypeListCriteria self() {
		return this;
	}
}
