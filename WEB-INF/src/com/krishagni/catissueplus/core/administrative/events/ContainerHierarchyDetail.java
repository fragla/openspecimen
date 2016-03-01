package com.krishagni.catissueplus.core.administrative.events;

import java.util.HashSet;
import java.util.Set;

public class ContainerHierarchyDetail {
    private Long containerTypeId;

    private String containerTypeName;

    private String siteName;

    private String parentContainer;

    private int numOfContainers;
    
    private Set<String> allowedSpecimenClasses = new HashSet<String>();
    
    private Set<String> allowedSpecimenTypes = new HashSet<String>();
    
    private Set<String> allowedCollectionProtocols = new HashSet<String>();

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

    public String getParentContainer() {
        return parentContainer;
    }

    public void setParentContainer(String parentContainer) {
        this.parentContainer = parentContainer;
    }
    
    public int getNumOfContainers() {
	return numOfContainers;
    }

    public void setNumOfContainers(int numOfContainers) {
	this.numOfContainers = numOfContainers;
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
