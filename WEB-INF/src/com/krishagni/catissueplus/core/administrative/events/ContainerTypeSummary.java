package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;

public class ContainerTypeSummary {
	private Long id;
	
	private String name;
	
	private int noOfColumns;
	
	private int noOfRows;
	
	private String abbreviation;
	
	private Double temperature;
	
	private String activityStatus;

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

	public String getAbbreviation() {
		return abbreviation;
	}

	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	public Double getTemperature() {
		return temperature;
	}

	public void setTemperature(Double temperature) {
		this.temperature = temperature;
	}
	
	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static <T extends ContainerTypeSummary> void copy(ContainerType containerType, T detail) {
		detail.setId(containerType.getId());
		detail.setName(containerType.getName());
		detail.setAbbreviation(containerType.getAbbreviation());
		detail.setNoOfColumns(containerType.getNoOfColumns());
		detail.setNoOfRows(containerType.getNoOfRows());
		detail.setTemperature(containerType.getTemperature());
		detail.setActivityStatus(containerType.getActivityStatus());
	}
	
	public static ContainerTypeSummary from (ContainerType containerType) {
		ContainerTypeSummary result = new ContainerTypeSummary();
		copy(containerType, result);
		return result;
	}
	
	public static List<ContainerTypeSummary> from (List<ContainerType> containerTypes) {
		return containerTypes.stream()
			.map(ContainerTypeSummary::from)
			.collect(Collectors.toList());
	}
}
