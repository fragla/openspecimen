
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;

public class ContainerTypeDetail extends ContainerTypeSummary {
	private String columnLabelingScheme;
	
	private String rowLabelingScheme;
	
	private boolean storeSpecimenEnabled;
	
	private ContainerTypeSummary canHold;

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

	public boolean isStoreSpecimenEnabled() {
		return storeSpecimenEnabled;
	}

	public void setStoreSpecimenEnabled(boolean storeSpecimenEnabled) {
		this.storeSpecimenEnabled = storeSpecimenEnabled;
	}

	public ContainerTypeSummary getCanHold() {
		return canHold;
	}

	public void setCanHold(ContainerTypeSummary canHold) {
		this.canHold = canHold;
	}

	public static ContainerTypeDetail from(ContainerType containerType) {
		if (containerType == null) {
			return null;
		}
		
		ContainerTypeDetail detail = new ContainerTypeDetail();
		ContainerTypeDetail.copy(containerType, detail);
		detail.setColumnLabelingScheme(containerType.getColumnLabelingScheme());
		detail.setRowLabelingScheme(containerType.getRowLabelingScheme());
		detail.setStoreSpecimenEnabled(containerType.isStoreSpecimenEnabled());
		detail.setCanHold(ContainerTypeSummary.from(containerType.getCanHold()));
		return detail;
	}
}
