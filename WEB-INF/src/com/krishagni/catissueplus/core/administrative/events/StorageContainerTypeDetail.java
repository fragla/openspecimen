
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainerType;

public class StorageContainerTypeDetail {
	private Long id;
	
	private String name;
	
	private int noOfRows;
	
	private int noOfColumns;
	
	private String rowLabelingScheme;
	
	private String columnLabelingScheme;
	
	private Double temperature;
	
	private boolean canStoreSpecimen;

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

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public int getNoOfColumns() {
		return noOfColumns;
	}

	public void setNoOfColumns(int noOfColumns) {
		this.noOfColumns = noOfColumns;
	}

	public String getRowLabelingScheme() {
		return rowLabelingScheme;
	}

	public void setRowLabelingScheme(String rowLabelingScheme) {
		this.rowLabelingScheme = rowLabelingScheme;
	}

	public String getColumnLabelingScheme() {
		return columnLabelingScheme;
	}

	public void setColumnLabelingScheme(String columnLabelingScheme) {
		this.columnLabelingScheme = columnLabelingScheme;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	
	public boolean isCanStoreSpecimen() {
		return canStoreSpecimen;
	}

	public void setCanStoreSpecimen(boolean canStoreSpecimen) {
		this.canStoreSpecimen = canStoreSpecimen;
	}

	public static StorageContainerTypeDetail from(StorageContainerType containerType) {
		StorageContainerTypeDetail result = new StorageContainerTypeDetail();
		
		result.setId(containerType.getId());
		result.setName(containerType.getName());
		result.setNoOfRows(containerType.getNoOfRows());
		result.setNoOfColumns(containerType.getNoOfColumns());
		result.setRowLabelingScheme(containerType.getRowLabelingScheme());
		result.setColumnLabelingScheme(containerType.getColumnLabelingScheme());
		result.setCanStoreSpecimen(containerType.isCanStoreSpecimen());
		result.setTemperature(containerType.getTemperature());
		
		return result;
	}
}

