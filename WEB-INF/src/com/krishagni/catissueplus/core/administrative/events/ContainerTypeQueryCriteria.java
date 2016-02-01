
package com.krishagni.catissueplus.core.administrative.events;

public class ContainerTypeQueryCriteria {	
	private Long id;
	
	private String name;
	
	public ContainerTypeQueryCriteria(Long id) {
		this.id = id;
	}
	
	public ContainerTypeQueryCriteria(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
