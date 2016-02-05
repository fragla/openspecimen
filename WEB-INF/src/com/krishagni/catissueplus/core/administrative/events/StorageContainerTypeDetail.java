
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerType;

public class StorageContainerTypeDetail {
	private Long id;
	
	private String name;
	
	private int noOfColumns;
	
	private int noOfRows;
	
	private String columnLabelingScheme;
	
	private String rowLabelingScheme;
	
	private Double temperature;
	
	private boolean storeSpecimenEnabled;
	
	private String abbreviation;
	
	private StorageContainerTypeDetail canHold;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public String getColumnLabelingScheme() {
		return columnLabelingScheme;
	}

	public void setColumnLabelingScheme(String columnLabelingScheme) {
		this.columnLabelingScheme = columnLabelingScheme;
	}

	public String getRowLabelingScheme() {
		return rowLabelingScheme;
	}

	public void setRowLabelingScheme(String rowLabelingScheme) {
		this.rowLabelingScheme = rowLabelingScheme;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}

	public boolean isStoreSpecimenEnabled() {
		return storeSpecimenEnabled;
	}

	public void setStoreSpecimenEnabled(boolean storeSpecimenEnabled) {
		this.storeSpecimenEnabled = storeSpecimenEnabled;
	}

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public StorageContainerTypeDetail getCanHold() {
		return canHold;
	}

	public void setCanHold(StorageContainerTypeDetail canHold) {
		this.canHold = canHold;
	}

	public static StorageContainerTypeDetail from(StorageContainerType containerType) {
		if (containerType == null) {
			return null;
		}
		
		StorageContainerTypeDetail result = new StorageContainerTypeDetail();
		result.setId(containerType.getId());
		result.setName(containerType.getName());
		result.setNoOfColumns(containerType.getNoOfColumns());
		result.setNoOfRows(containerType.getNoOfRows());
		result.setColumnLabelingScheme(containerType.getColumnLabelingScheme());
		result.setRowLabelingScheme(containerType.getRowLabelingScheme());
		result.setTemperature(containerType.getTemperature());
		result.setStoreSpecimenEnabled(containerType.isStoreSpecimenEnabled());
		result.setAbbreviation(containerType.getAbbreviation());
		result.setCanHold(StorageContainerTypeDetail.from(containerType.getCanHold()));
		return result;
	}
}
