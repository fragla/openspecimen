package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.ContainerTypeQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ContainerTypeService {	
	public ResponseEvent<ContainerTypeDetail> getContainerType(RequestEvent<ContainerTypeQueryCriteria> req);

	public ResponseEvent<ContainerTypeDetail> createContainerType(RequestEvent<ContainerTypeDetail> req);
	
	public ResponseEvent<ContainerTypeDetail> updateContainerType(RequestEvent<ContainerTypeDetail> req);
	
}
