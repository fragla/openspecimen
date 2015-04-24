package com.krishagni.catissueplus.core.common.events;

import org.apache.commons.lang.StringUtils;

public enum Resource {
	CP("CollectionProtocol"),
	
	PARTICIPANT("ParticipantPhi"),
	
	PARTICIPANT_DEID("ParticipantDeid"),
	
	VISIT_N_SPECIMEN("VisitAndSpecimen"),
	
	STORAGE_CONTAINER("StorageContainer"),
	
	USER("User"),
	
	ORDER("Order"),
	
	DP("DistributionProtocol");

	private final String name;
	
	private Resource(String name) {
		this.name = name;
	}
	
	public String getName() { 
		return name;
	}
	
	public static Resource fromName(String name) {
		if (StringUtils.isNotBlank(name)) {
			for (Resource r : Resource.values()) {
				if (r.name.equalsIgnoreCase(name)) {
					return r;
				}
			}
		}
		return null;
	}
	
}
