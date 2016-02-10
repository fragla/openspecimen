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
import com.krishagni.catissueplus.core.common.access.AccessCtrlMgr;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainerTypeServiceImpl implements StorageContainerTypeService {
	private DaoFactory daoFactory;
	
	private StorageContainerTypeFactory containerTypeFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setContainerTypeFactory(StorageContainerTypeFactory containerTypeFactory) {
		this.containerTypeFactory = containerTypeFactory;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerTypeDetail> getStorageContainerType(
			RequestEvent<ContainerTypeQueryCriteria> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			StorageContainerType containerType = getContainerType(req.getPayload());
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
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			StorageContainerTypeDetail input = req.getPayload();
			StorageContainerType containerType = containerTypeFactory.createStorageContainerType(input);
			
			ensureUniqueConstraints(null, containerType);
			daoFactory.getStorageContainerTypeDao().saveOrUpdate(containerType, true);
			return ResponseEvent.response(StorageContainerTypeDetail.from(containerType));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<StorageContainerTypeDetail> updateStorageContainerType(
			RequestEvent<StorageContainerTypeDetail> req) {
		try {
			AccessCtrlMgr.getInstance().ensureUserIsAdmin();
			StorageContainerTypeDetail input = req.getPayload();
			StorageContainerType existing = getContainerType(input.getId(), input.getName());
			StorageContainerType containerType = containerTypeFactory.createStorageContainerType(input);
			
			ensureUniqueConstraints(existing, containerType);
			existing.update(containerType);
			daoFactory.getStorageContainerTypeDao().saveOrUpdate(existing, true);
			return ResponseEvent.response(StorageContainerTypeDetail.from(existing));
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
		StorageContainerType containerType = null;
		Object key = null;
		if (id != null) {
			containerType = daoFactory.getStorageContainerTypeDao().getById(id);
			key = id;
		} else if (StringUtils.isNotBlank(name)) {
			containerType = daoFactory.getStorageContainerTypeDao().getByName(name);
			key = name;
		}
		
		if (containerType == null) {
			throw OpenSpecimenException.userError(StorageContainerTypeErrorCode.NOT_FOUND, key);
		}
		
		return containerType;
	}
	
	private void ensureUniqueConstraints(StorageContainerType existing, StorageContainerType newContainerType) {
		if (!isUniqueName(existing, newContainerType)) {
			throw OpenSpecimenException.userError(StorageContainerTypeErrorCode.DUP_NAME, newContainerType.getName());
		}
	}
	
	private boolean isUniqueName(StorageContainerType existingContainerType, StorageContainerType newContainerType) {
		if (existingContainerType != null && existingContainerType.getName().equals(newContainerType.getName())) {
			return true;
		}
		
		StorageContainerType containerType = daoFactory.getStorageContainerTypeDao().
				getByName(newContainerType.getName());
		return containerType == null;
	}

}
