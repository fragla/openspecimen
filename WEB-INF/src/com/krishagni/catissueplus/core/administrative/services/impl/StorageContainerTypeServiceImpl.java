package com.krishagni.catissueplus.core.administrative.services.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerType;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerTypeErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerTypeFactory;
import com.krishagni.catissueplus.core.administrative.events.ContainerQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerTypeDetail;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerTypeService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainerTypeServiceImpl implements StorageContainerTypeService {
	private DaoFactory daoFactory;
	
	private StorageContainerTypeFactory containerTypeFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public StorageContainerTypeFactory getContainerTypeFactory() {
		return containerTypeFactory;
	}

	public void setContainerTypeFactory(StorageContainerTypeFactory containerTypeFactory) {
		this.containerTypeFactory = containerTypeFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerTypeDetail> createStorageContainerType(RequestEvent<StorageContainerTypeDetail> req) {
		try {
			StorageContainerTypeDetail input = req.getPayload();
			StorageContainerType containerType = containerTypeFactory.createStorageContainerType(input);
			
			ensureUniqueConstraintsType(null, containerType);
			//container.validateRestrictions();
			daoFactory.getStorageContainerTypeDao().saveOrUpdate(containerType, true);
			return ResponseEvent.response(StorageContainerTypeDetail.from(containerType));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueConstraintsType(StorageContainerType existing, StorageContainerType newContainerType) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		if (!isUniqueName1(existing, newContainerType)) {
			ose.addError(StorageContainerErrorCode.DUP_NAME, newContainerType.getName());
		}
		
		ose.checkAndThrow();
	}
	
	private boolean isUniqueName1(StorageContainerType existingContainerType, StorageContainerType newContainerType) {
		if (existingContainerType != null && existingContainerType.getName().equals(newContainerType.getName())) {
			return true;
		}
		
		StorageContainerType container = daoFactory.getStorageContainerTypeDao().getByName(newContainerType.getName());
		return container == null;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerTypeDetail> getStorageContainerType(RequestEvent<ContainerTypeQueryCriteria> req) {
		try {		
			StorageContainerType containerType = getContainerType(req.getPayload());						
//			AccessCtrlMgr.getInstance().ensureReadContainerRights(container);
			return ResponseEvent.response(StorageContainerTypeDetail.from(containerType));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	
	private StorageContainerType getContainerType(ContainerTypeQueryCriteria crit) {
		return getContainerType(crit.getId(), crit.getName());
	}
	
	private StorageContainerType getContainerType(Long id, String name) {
		return getContainerType(id, name, StorageContainerTypeErrorCode.ID_OR_NAME_REQ);
	}
	
	private StorageContainerType getContainerType(Long id, String name, ErrorCode requiredErrCode) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		StorageContainerType containerType = null;
		if (id != null) {
			containerType = daoFactory.getStorageContainerTypeDao().getById(id);
			if (containerType == null) {
				ose.addError(StorageContainerTypeErrorCode.NOT_FOUND, id);
			}
		} else if (StringUtils.isNotBlank(name)) {
			containerType = daoFactory.getStorageContainerTypeDao().getByName(name);
			if (containerType == null) {
				ose.addError(StorageContainerTypeErrorCode.NOT_FOUND, name);
			}
		} else if (requiredErrCode != null) {
			ose.addError(requiredErrCode);
		}
		
		ose.checkAndThrow();
		return containerType;
	}



}
