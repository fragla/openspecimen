package com.krishagni.catissueplus.core.administrative.events;

public class ContainerHierarchyDetail extends StorageContainerDetail {
	private int numOfContainers;
	
	private Long containerTypeId;

	private String containerTypeName;

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
}
