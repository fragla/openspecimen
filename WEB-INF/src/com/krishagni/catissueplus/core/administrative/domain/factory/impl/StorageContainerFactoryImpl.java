package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import static com.krishagni.catissueplus.core.common.PvAttributes.SPECIMEN_CLASS;
import static com.krishagni.catissueplus.core.common.service.PvValidator.areValid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.domain.factory.ContainerTypeErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.SiteErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerFactory;
import com.krishagni.catissueplus.core.administrative.domain.factory.UserErrorCode;
import com.krishagni.catissueplus.core.administrative.events.ContainerHierarchyDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageLocationSummary;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SpecimenErrorCode;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class StorageContainerFactoryImpl implements StorageContainerFactory {
	private DaoFactory daoFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public StorageContainer createStorageContainer(StorageContainerDetail detail) {
		StorageContainer container = new StorageContainer();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		container.setId(detail.getId());
		container.setStoreSpecimenEnabled(detail.isStoreSpecimensEnabled());
		
		setName(detail, container, ose);
		setBarcode(detail, container, ose);
		setContainerType(detail, container, ose);
		setTemperature(detail, container, ose);
		setCapacity(detail, container, ose);
		setLabelingSchemes(detail, container, ose);
		setSiteAndParentContainer(detail, container, ose);
		setPosition(detail, container, ose);
		setCreatedBy(detail, container, ose);
		setActivityStatus(detail, container, ose);
		setComments(detail, container, ose);
		setAllowedSpecimenClasses(detail, container, ose);
		setAllowedSpecimenTypes(detail, container, ose);
		setAllowedCps(detail, container, ose);
		setComputedRestrictions(container);
		
		ose.checkAndThrow();
		return container;
	}

	@Override
	public StorageContainer createStorageContainer(StorageContainer existing, StorageContainerDetail detail) {
		StorageContainer container = new StorageContainer();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		
		container.setId(existing.getId());
		if (detail.isAttrModified("storeSpecimensEnabled")) {
			container.setStoreSpecimenEnabled(detail.isStoreSpecimensEnabled());
		} else {
			container.setStoreSpecimenEnabled(existing.isStoreSpecimenEnabled());
		}
		
		setName(detail, existing, container, ose);
		setBarcode(detail, existing, container, ose);
		setContainerType(detail, existing, container, ose);
		setTemperature(detail, existing, container, ose);
		setCapacity(detail, existing, container, ose);
		setLabelingSchemes(detail, existing, container, ose);
		setSiteAndParentContainer(detail, existing, container, ose);
		setPosition(detail, existing, container, ose);
		setCreatedBy(detail, existing, container, ose);
		setActivityStatus(detail, existing, container, ose);
		setComments(detail, existing, container, ose);
		setAllowedSpecimenClasses(detail, existing, container, ose);
		setAllowedSpecimenTypes(detail, existing, container, ose);
		setAllowedCps(detail, existing, container, ose);
		setComputedRestrictions(container);
		
		ose.checkAndThrow();
		return container;
	}
	
	@Override
	public StorageContainer createStorageContainer(ContainerHierarchyDetail hierarchyDetail, String name) {
		ContainerType containerType = getContainerType(hierarchyDetail.getContainerTypeId(), 
				hierarchyDetail.getContainerTypeName());
		StorageContainerDetail detail = populateContainerDetail(containerType);
		detail.setName(name);
		detail.setSiteName(hierarchyDetail.getSiteName());
		detail.setStorageLocation(hierarchyDetail.getStorageLocation());
		detail.setAllowedSpecimenClasses(hierarchyDetail.getAllowedSpecimenClasses());
		detail.setAllowedSpecimenTypes(hierarchyDetail.getAllowedSpecimenTypes());
		detail.setAllowedCollectionProtocols(hierarchyDetail.getAllowedCollectionProtocols());
		
		if (hierarchyDetail.getNoOfColumns() != null) {
			detail.setNoOfColumns(hierarchyDetail.getNoOfColumns());
		}
		
		if (hierarchyDetail.getNoOfRows() != null) {
			detail.setNoOfRows(hierarchyDetail.getNoOfRows());
		}
		
		if (StringUtils.isNotBlank(hierarchyDetail.getColumnLabelingScheme())) {
			detail.setColumnLabelingScheme(hierarchyDetail.getColumnLabelingScheme());
		}
		
		if (StringUtils.isNotBlank(hierarchyDetail.getRowLabelingScheme())) {
			detail.setRowLabelingScheme(hierarchyDetail.getRowLabelingScheme());
		}
		
		if (hierarchyDetail.getTemperature() != null) {
			detail.setTemperature(hierarchyDetail.getTemperature());
		}
		
		if (hierarchyDetail.getStoreSpecimensEnabled() != null) {
			detail.setStoreSpecimensEnabled(hierarchyDetail.getStoreSpecimensEnabled());
		}
		
		return createStorageContainer(detail);
	}
	
	@Override
	public StorageContainer createStorageContainer(ContainerType containerType, StorageContainer parentContainer, String name) {
		StorageContainerDetail detail = populateContainerDetail(containerType);
		detail.setName(name);
		StorageLocationSummary storageLocation = new StorageLocationSummary();
		storageLocation.setName(parentContainer.getName());
		detail.setStorageLocation(storageLocation);
		
		return createStorageContainer(detail);
	}
	
	private void setName(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(StorageContainerErrorCode.NAME_REQUIRED);
			return;
		}
		
		container.setName(name);
	}
	
	private void setName(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("name")) {
			setName(detail, container, ose);
		} else {
			container.setName(existing.getName());
		}
	}
	
	private void setBarcode(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		container.setBarcode(detail.getBarcode());
	}
	
	private void setBarcode(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("barcode")) {
			setBarcode(detail, container, ose);
		} else {
			container.setBarcode(existing.getBarcode());
		}
	}
	
	private void setContainerType(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		if (detail.getContainerTypeId() == null && StringUtils.isBlank(detail.getContainerTypeName())) {
			return;
		}
		
		ContainerType containerType = getContainerType(detail.getContainerTypeId(), detail.getContainerTypeName());
		container.setContainerType(containerType);
	}
	
	private void setContainerType(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("containerTypeId") || detail.isAttrModified("containerTypeName")) {
			setContainerType(detail, container, ose);
		} else {
			container.setContainerType(existing.getContainerType());
		}
	}	
	
	private void setTemperature(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		container.setTemperature(detail.getTemperature());
	}
	
	private void setTemperature(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("temperature")) {
			setTemperature(detail, container, ose);
		} else {
			container.setTemperature(existing.getTemperature());
		}
	}
		
	private void setCapacity(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		setNoOfColumns(detail, container, ose);
		setNoOfRows(detail, container, ose);
	}
	
	private void setCapacity(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("noOfColumns")) {
			setNoOfColumns(detail, container, ose);
		} else {
			container.setNoOfColumns(existing.getNoOfColumns());
		}
		
		if (detail.isAttrModified("noOfRows")) {
			setNoOfRows(detail, container, ose);
		} else {
			container.setNoOfRows(existing.getNoOfRows());
		}
	}
	
	private void setNoOfColumns(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		int noOfCols = detail.getNoOfColumns();		
		if (noOfCols <= 0) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_CAPACITY);			
		}
		
		container.setNoOfColumns(noOfCols);		
	}
	
	private void setNoOfRows(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		int noOfRows = detail.getNoOfRows();
		if (noOfRows <= 0) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_CAPACITY);
		}
				
		container.setNoOfRows(noOfRows);		
	}
	
	private void setLabelingSchemes(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		setColumnLabelingScheme(detail, container, ose);
		setRowLabelingScheme(detail, container, ose);
	}
	
	private void setLabelingSchemes(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("columnLabelingScheme")) {
			setColumnLabelingScheme(detail, container, ose);
		} else {
			container.setColumnLabelingScheme(existing.getColumnLabelingScheme());
		}
		
		if (detail.isAttrModified("rowLabelingScheme")) {
			setRowLabelingScheme(detail, container, ose);
		} else {
			container.setRowLabelingScheme(existing.getRowLabelingScheme());
		}
	}
	
	private void setColumnLabelingScheme(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String columnLabelingScheme = detail.getColumnLabelingScheme();
		if (StringUtils.isBlank(columnLabelingScheme)) {
			columnLabelingScheme = StorageContainer.NUMBER_LABELING_SCHEME;
		}
		
		if (!StorageContainer.isValidScheme(columnLabelingScheme)) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_LABELING_SCHEME);
		}
		
		container.setColumnLabelingScheme(columnLabelingScheme);		
	}
	
	private void setRowLabelingScheme(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String rowLabelingScheme = detail.getRowLabelingScheme();
		if (StringUtils.isBlank(rowLabelingScheme)) {
			rowLabelingScheme = container.getColumnLabelingScheme();
		}
		
		if (!StorageContainer.isValidScheme(rowLabelingScheme)) {
			ose.addError(StorageContainerErrorCode.INVALID_DIMENSION_LABELING_SCHEME);
		}
		
		container.setRowLabelingScheme(rowLabelingScheme);		
	}
		
	private void setSiteAndParentContainer(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Site site = setSite(detail, container, ose);
		StorageContainer parentContainer = setParentContainer(detail, container, ose);
		
		if (site == null && parentContainer == null) {
			ose.addError(StorageContainerErrorCode.REQUIRED_SITE_OR_PARENT_CONT);
			return;
		}
		
		if (site == null) {
			container.setSite(parentContainer.getSite());
		} else if (parentContainer != null && !parentContainer.getSite().equals(site)) {
			ose.addError(StorageContainerErrorCode.INVALID_SITE_AND_PARENT_CONT);
		}
	}
	
	private void setSiteAndParentContainer(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("siteName") || detail.isAttrModified("storageLocation")) {
			setSiteAndParentContainer(detail, container, ose);
		} else {
			container.setSite(existing.getSite());
			container.setParentContainer(existing.getParentContainer());
		}
	}
	
	private Site setSite(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String siteName = detail.getSiteName();
		if (StringUtils.isBlank(siteName)) {
			return null;
		}
				
		Site site = daoFactory.getSiteDao().getSiteByName(siteName);
		if (site == null) {
			ose.addError(SiteErrorCode.NOT_FOUND);			
		}
			
		container.setSite(site);
		return site;		
	}
	
	private StorageContainer setParentContainer(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		StorageContainer parentContainer = null;
		Object key = null;
		
		StorageLocationSummary storageLocation = detail.getStorageLocation();
		if (storageLocation == null) {
			return null;
		}
		
		if (storageLocation.getId() != null) {
			parentContainer = daoFactory.getStorageContainerDao().getById(storageLocation.getId());
			key = storageLocation.getId();
		} else if (StringUtils.isNotBlank(storageLocation.getName())) {
			parentContainer = daoFactory.getStorageContainerDao().getByName(storageLocation.getName());
			key = storageLocation.getName();
		}
		
		if (parentContainer == null) { 
			if (key != null) {
				ose.addError(StorageContainerErrorCode.PARENT_CONT_NOT_FOUND, key);
			}
			
			return null;
		}
		
		container.setParentContainer(parentContainer);
		return parentContainer;
	}
	
	private void setPosition(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		StorageContainer parentContainer = container.getParentContainer();
		String posOne = null, posTwo = null;
		
		StorageLocationSummary storageLocation = detail.getStorageLocation();
		if (storageLocation != null) {
			posOne = storageLocation.getPositionX();
			posTwo = storageLocation.getPositionY();
		}
				
		if (parentContainer == null) { // top-level container; therefore no position
			return;
		}
		
		StorageContainerPosition position = null;
		if (StringUtils.isNotBlank(posOne) && StringUtils.isNotBlank(posTwo)) {
			if (parentContainer.canContainerOccupyPosition(container.getId(), posOne, posTwo)) {
				position = parentContainer.createPosition(posOne, posTwo);
			} else {
				ose.addError(StorageContainerErrorCode.NO_FREE_SPACE, parentContainer.getName());
			}
		} else {
			position = parentContainer.nextAvailablePosition();
			if (position == null) {
				ose.addError(StorageContainerErrorCode.NO_FREE_SPACE, parentContainer.getName());
			} 
		} 
		
		if (position != null) {
			position.setOccupyingContainer(container);
			container.setPosition(position);			
		}
	}
	
	private void setPosition(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("storageLocation")) {
			setPosition(detail, container, ose);
		} else {
			container.setPosition(existing.getPosition());
		}
	}
	
	private void setCreatedBy(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Long userId = null;
		if (detail.getCreatedBy() == null) {
			userId = AuthUtil.getCurrentUser().getId();
		} else {
			userId = detail.getCreatedBy().getId();
		}

		User user = daoFactory.getUserDao().getById(userId);
		if (user == null) {
			ose.addError(UserErrorCode.NOT_FOUND);
			return;
		}
		
		container.setCreatedBy(user);
	}
	
	private void setCreatedBy(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("createdBy")) {
			setCreatedBy(detail, container, ose);
		} else {
			container.setCreatedBy(existing.getCreatedBy());
		}
	}
	
	private void setActivityStatus(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (activityStatus == null) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}
		
		container.setActivityStatus(activityStatus);
	}
	
	private void setActivityStatus(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("activityStatus")) {
			setActivityStatus(detail, container, ose);
		} else {
			container.setActivityStatus(existing.getActivityStatus());
		}
	}
	
	private void setComments(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		container.setComments(detail.getComments());
	}	
	
	private void setComments(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("comments")) {
			setComments(detail, container, ose);
		} else {
			container.setComments(existing.getComments());
		}
	}
	
	private void setAllowedSpecimenClasses(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Set<String> allowedSpecimenClasses = detail.getAllowedSpecimenClasses();		
		if (!areValid(SPECIMEN_CLASS, allowedSpecimenClasses)) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_CLASS);
			return;
		}
						
		container.setAllowedSpecimenClasses(allowedSpecimenClasses);		
	}
	
	private void setAllowedSpecimenClasses(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("allowedSpecimenClasses")) {
			setAllowedSpecimenClasses(detail, container, ose);
		} else {
			container.setAllowedSpecimenClasses(existing.getAllowedSpecimenClasses());
		}
	}
	
	private void setAllowedSpecimenTypes(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Set<String> allowedSpecimenTypes = detail.getAllowedSpecimenTypes();
		if (!areValid(SPECIMEN_CLASS, 1, allowedSpecimenTypes)) {
			ose.addError(SpecimenErrorCode.INVALID_SPECIMEN_TYPE);
			return;
		}
						
		container.setAllowedSpecimenTypes(allowedSpecimenTypes);		
	}
	
	private void setAllowedSpecimenTypes(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("allowedSpecimenTypes")) {
			setAllowedSpecimenTypes(detail, container, ose);
		} else {
			container.setAllowedSpecimenTypes(existing.getAllowedSpecimenTypes());
		}
	}
	
	private void setAllowedCps(StorageContainerDetail detail, StorageContainer container, OpenSpecimenException ose) {
		Set<String> allowedCps = detail.getAllowedCollectionProtocols();
		
		List<CollectionProtocol> cps = new ArrayList<CollectionProtocol>();		
		if (CollectionUtils.isNotEmpty(allowedCps) && container.getSite() != null) {
			cps = daoFactory.getCollectionProtocolDao().getCpsByShortTitle(allowedCps, container.getSite().getName());
			if (cps.size() != allowedCps.size()) {
				ose.addError(StorageContainerErrorCode.INVALID_CPS);
				return;
			}			
		}

		container.setAllowedCps(new HashSet<CollectionProtocol>(cps));		
	}
	
	private void setAllowedCps(StorageContainerDetail detail, StorageContainer existing, StorageContainer container, OpenSpecimenException ose) {
		if (detail.isAttrModified("allowedCollectionProtocols")) {
			setAllowedCps(detail, container, ose);
		} else {
			container.setAllowedCps(existing.getAllowedCps());
		}		
	}
	
	private void setComputedRestrictions(StorageContainer container) {
		container.setCompAllowedSpecimenClasses(container.computeAllowedSpecimenClasses());
		container.setCompAllowedSpecimenTypes(container.computeAllowedSpecimenTypes());
		container.setCompAllowedCps(container.computeAllowedCps());
	}
	
	private ContainerType getContainerType(Long id, String name) {
		ContainerType containerType = null;
		Object key = null;
		if (id != null) {
			containerType = daoFactory.getContainerTypeDao().getById(id);
			key = id;
		} else if (StringUtils.isNotBlank(name)) {
			containerType = daoFactory.getContainerTypeDao().getByName(name);
			key = name;
		} else {
			throw OpenSpecimenException.userError(StorageContainerErrorCode.TYPE_REQUIRED);
		}
		
		if (containerType == null) {
			throw OpenSpecimenException.userError(ContainerTypeErrorCode.NOT_FOUND, key);
		}
		return containerType;
	}
	

	private StorageContainerDetail populateContainerDetail(ContainerType containerType) {
		StorageContainerDetail detail = new StorageContainerDetail();
		detail.setNoOfColumns(containerType.getNoOfColumns());
		detail.setNoOfRows(containerType.getNoOfRows());
		detail.setColumnLabelingScheme(containerType.getColumnLabelingScheme());
		detail.setRowLabelingScheme(containerType.getRowLabelingScheme());
		detail.setTemperature(containerType.getTemperature());
		detail.setStoreSpecimensEnabled(containerType.isStoreSpecimenEnabled());
		return detail;
	}
}
