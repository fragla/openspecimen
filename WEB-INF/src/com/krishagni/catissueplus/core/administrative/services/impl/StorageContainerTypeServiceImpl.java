package com.krishagni.catissueplus.core.administrative.services.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerType;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerTypeErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerTypeFactory;
import com.krishagni.catissueplus.core.administrative.events.ContainerTypeQueryCriteria;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerTypeDetail;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerTypeService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;

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
	public ResponseEvent<StorageContainerTypeDetail> getStorageContainerType(
			                 RequestEvent<ContainerTypeQueryCriteria> req) {
		try {
			StorageContainerType containerType = getContainerType(req.getPayload());			
			if (!AuthUtil.isAdmin()) {
				throw new OpenSpecimenException(ErrorType.USER_ERROR, RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
			}
			return ResponseEvent.response(StorageContainerTypeDetail.from(containerType));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerTypeDetail> createStorageContainerType(
			                 RequestEvent<StorageContainerTypeDetail> req) {
		try {
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			StorageContainerTypeDetail input = req.getPayload();
			StorageContainerType containerType = containerTypeFactory.createStorageContainerType(input);
			if (!AuthUtil.isAdmin()) {
				throw new OpenSpecimenException(ErrorType.USER_ERROR, RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
			}
			
			ensureUniqueConstraints(null, containerType, ose);
			daoFactory.getStorageContainerTypeDao().saveOrUpdate(containerType, true);
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
				ose.addError(StorageContainerTypeErrorCode.ID_NOT_FOUND, id);
			}
		} else if (StringUtils.isNotBlank(name)) {
			containerType = daoFactory.getStorageContainerTypeDao().getByName(name);
			if (containerType == null) {
				ose.addError(StorageContainerTypeErrorCode.NAME_NOT_FOUND, name);
			}
		} else if (requiredErrCode != null) {
			ose.addError(requiredErrCode);
		}
		
		ose.checkAndThrow();
		return containerType;
	}
	
	private void ensureUniqueConstraints(StorageContainerType existing, StorageContainerType newContainerType, 
			                                  OpenSpecimenException ose) {
		if (!isUniqueName(existing, newContainerType)) {
			ose.addError(StorageContainerTypeErrorCode.DUP_NAME, newContainerType.getName());
		}
		
		ose.checkAndThrow();
	}
	
	private boolean isUniqueName(StorageContainerType existingContainerType, StorageContainerType newContainerType) {
		StorageContainerType containerType = daoFactory.getStorageContainerTypeDao().getByName(newContainerType
				                                       .getName());
		return containerType == null;
	}

}
