package com.krishagni.catissueplus.core.administrative.events;

import java.util.HashSet;
import java.util.Set;

public class ContainerHierarchyDetail {
    private int numOfContainers;
    
    private Long containerTypeId;

    private String containerTypeName;

    private String siteName;
    
    private StorageLocationSummary storageLocation;

	private int noOfColumns;
	
	private int noOfRows;
	
	private String columnLabelingScheme;
	
	private String rowLabelingScheme;
	
	private Double temperature;
	
	private boolean storeSpecimensEnabled;
    
    private Set<String> allowedSpecimenClasses = new HashSet<String>();
    
    private Set<String> allowedSpecimenTypes = new HashSet<String>();
    
    private Set<String> allowedCollectionProtocols = new HashSet<String>();
    
    public int getNumOfContainers() {
	return numOfContainers;
    }

    public void setNumOfContainers(int numOfContainers) {
	this.numOfContainers = numOfContainers;
    }

    public Long getContainerTypeId() {
        return containerTypeId;
    }

    public void setContainerTypeId(Long containerTypeId) {
        this.containerTypeId = containerTypeId;
    }

    public String getContainerTypeName() {
        return containerTypeName;
    }

    public void setContainerTypeName(String containerTypeName) {
        this.containerTypeName = containerTypeName;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public StorageLocationSummary getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(StorageLocationSummary storageLocation) {
		this.storageLocation = storageLocation;
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

	public boolean isStoreSpecimensEnabled() {
		return storeSpecimensEnabled;
	}

	public void setStoreSpecimensEnabled(boolean storeSpecimensEnabled) {
		this.storeSpecimensEnabled = storeSpecimensEnabled;
	}

	public Set<String> getAllowedSpecimenClasses() {
    	return allowedSpecimenClasses;
    }
	
    public void setAllowedSpecimenClasses(Set<String> allowedSpecimenClasses) {
    	this.allowedSpecimenClasses = allowedSpecimenClasses;
    }
	
    public Set<String> getAllowedSpecimenTypes() {
    	return allowedSpecimenTypes;
    }
	
    public void setAllowedSpecimenTypes(Set<String> allowedSpecimenTypes) {
    	this.allowedSpecimenTypes = allowedSpecimenTypes;
    }
	
    public Set<String> getAllowedCollectionProtocols() {
    	return allowedCollectionProtocols;
    }
	
    public void setAllowedCollectionProtocols(Set<String> allowedCollectionProtocols) {
	this.allowedCollectionProtocols = allowedCollectionProtocols;
    }
}
