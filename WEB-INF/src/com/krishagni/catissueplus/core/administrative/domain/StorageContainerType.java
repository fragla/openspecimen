package com.krishagni.catissueplus.core.administrative.domain;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class StorageContainerType extends BaseEntity {
	private String name;
	
	private int noOfColumns;
	
	private int noOfRows;
	
	private String columnLabelingScheme;
	
	private String rowLabelingScheme;
	
	private Double temperature;
	
	private boolean storeSpecimenEnabled = false;
	
	private String abbreviation;
	
	private StorageContainerType canHold;
	
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

	public StorageContainerType getCanHold() {
		return canHold;
	}

	public void setCanHold(StorageContainerType canHold) {
		this.canHold = canHold;
	}
	
	public void update(StorageContainerType containerType) {
		setName(containerType.name);
		setNoOfColumns(containerType.noOfColumns);
		setNoOfRows(containerType.noOfRows);
		setColumnLabelingScheme(containerType.columnLabelingScheme);
		setRowLabelingScheme(containerType.rowLabelingScheme);
		setTemperature(containerType.temperature);
		setStoreSpecimenEnabled(containerType.storeSpecimenEnabled);
		setAbbreviation(containerType.abbreviation);
		setCanHold(containerType.canHold);
	}

}
