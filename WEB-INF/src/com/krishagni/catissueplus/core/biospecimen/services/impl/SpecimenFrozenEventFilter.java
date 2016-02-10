package com.krishagni.catissueplus.core.biospecimen.services.impl;

import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataFilter;

public class SpecimenFrozenEventFilter implements FormDataFilter {

	@Override
	public FormData execute(UserContext userCtx, FormData input) {
		//write code to update specimen freeze thaw cycle number if checkbox is selected
		return null;
	}
}
