package com.krishagni.catissueplus.core.administrative.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerType;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerTypeErrorCode;
import com.krishagni.catissueplus.core.administrative.domain.factory.StorageContainerTypeFactory;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerTypeDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class StorageContainerTypeFactoryImpl implements StorageContainerTypeFactory{
	private DaoFactory daoFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public StorageContainerType createStorageContainerType(StorageContainerTypeDetail detail, StorageContainerType canHold) {
		StorageContainerType containerType = new StorageContainerType();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
		containerType.setId(detail.getId());
		containerType.setCanStoreSpecimen(detail.isCanStoreSpecimen());
		setName(detail, containerType, ose);
		setDimension(detail, containerType, ose);
		setLabelingSchemes(detail, containerType, ose);
		setTemperature(detail, containerType, ose);
		setAbbreviation(detail, containerType, ose);
		if(canHold != null) {
			containerType.setCanHold(canHold);
		}
		
		ose.checkAndThrow();
		return containerType;
	}
	
	private void setName(StorageContainerTypeDetail detail, StorageContainerType containerType, OpenSpecimenException ose) {
		String name = detail.getName();
		if (StringUtils.isBlank(name)) {
			ose.addError(StorageContainerTypeErrorCode.NAME_REQUIRED);
			return;
		}
		
		containerType.setName(name);
	}
	
	private void setTemperature(StorageContainerTypeDetail detail, StorageContainerType containerType, OpenSpecimenException ose) {
		containerType.setTemperature(detail.getTemperature());
	}
	
	private void setAbbreviation(StorageContainerTypeDetail detail, StorageContainerType containerType, OpenSpecimenException ose) {
		containerType.setAbbreviation(detail.getAbbreviation());
	}
		
	private void setDimension(StorageContainerTypeDetail detail, StorageContainerType containerType, OpenSpecimenException ose) {
		setNoOfColumns(detail, containerType, ose);
		setNoOfRows(detail, containerType, ose);
	}
	
	private void setNoOfColumns(StorageContainerTypeDetail detail, StorageContainerType containerType, OpenSpecimenException ose) {
		int noOfCols = detail.getNoOfColumns();		
		if (noOfCols <= 0) {
			ose.addError(StorageContainerTypeErrorCode.INVALID_DIMENSION_CAPACITY);			
		}
		
		containerType.setNoOfColumns(noOfCols);	
	}
	
	private void setNoOfRows(StorageContainerTypeDetail detail, StorageContainerType containerType, OpenSpecimenException ose) {
		int noOfRows = detail.getNoOfRows();
		if (noOfRows <= 0) {
			ose.addError(StorageContainerTypeErrorCode.INVALID_DIMENSION_CAPACITY);
		}
				
		containerType.setNoOfRows(noOfRows);		
	}
	
	private void setLabelingSchemes(StorageContainerTypeDetail detail, StorageContainerType containerType, OpenSpecimenException ose) {
		setColumnLabelingScheme(detail, containerType, ose);
		setRowLabelingScheme(detail, containerType, ose);
	}
	
	private void setColumnLabelingScheme(StorageContainerTypeDetail detail, StorageContainerType containerType, OpenSpecimenException ose) {
		String columnLabelingScheme = detail.getColumnLabelingScheme();
		if (StringUtils.isBlank(columnLabelingScheme)) {
			columnLabelingScheme = StorageContainer.NUMBER_LABELING_SCHEME;
		}
		
		if (!StorageContainer.isValidScheme(columnLabelingScheme)) {
			ose.addError(StorageContainerTypeErrorCode.INVALID_DIMENSION_LABELING_SCHEME);
		}
		
		containerType.setColumnLabelingScheme(columnLabelingScheme);		
	}
	
	private void setRowLabelingScheme(StorageContainerTypeDetail detail, StorageContainerType containerType, OpenSpecimenException ose) {
		String rowLabelingScheme = detail.getRowLabelingScheme();
		if (StringUtils.isBlank(rowLabelingScheme)) {
			rowLabelingScheme = containerType.getColumnLabelingScheme();
		}
		
		if (!StorageContainer.isValidScheme(rowLabelingScheme)) {
			ose.addError(StorageContainerTypeErrorCode.INVALID_DIMENSION_LABELING_SCHEME);
		}
		
		containerType.setRowLabelingScheme(rowLabelingScheme);		
	}

}
