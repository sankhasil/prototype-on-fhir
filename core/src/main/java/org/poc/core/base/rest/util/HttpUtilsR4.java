package org.poc.core.base.rest.util;

import org.apache.commons.lang3.StringUtils;
import org.poc.core.constants.HttpHeaders;
import org.hl7.fhir.r4.model.BaseResource;
import org.hl7.fhir.r4.model.StringType;

/**
 * 
 * @author Sankha
 *
 */
public class HttpUtilsR4 {


	private HttpUtilsR4() {

	}


	public static void setHeadersToPayloadForSave(Object object) {
		if (object instanceof BaseResource) {
			//FIXME: Use meta info to put multi tenant based data in database.
			// This is one corner scenario where data is also required to be stored with multi tenant data sign.
//			if (((BaseResource) object).getUnitId() == null || (((BaseResource) object).getUnitId() != null
//					&& StringUtils.isBlank(((BaseResource) object).getUnitId().asStringValue())))
//				((BaseResource) object).setUnitId(new StringType(HttpUtils.getHeader(HttpHeaders.UNIT_ID)));
//			if (((BaseResource) object).getUnitCode() == null || ((BaseResource) object).getUnitCode() != null
//					&& StringUtils.isBlank(((BaseResource) object).getUnitCode().asStringValue()))
//				((BaseResource) object).setUnitCode(new StringType(HttpUtils.getHeader(HttpHeaders.UNIT_CODE)));
//			if (((BaseResource) object).getOrgId() == null || ((BaseResource) object).getOrgId() != null
//					&& StringUtils.isBlank(((BaseResource) object).getOrgId().asStringValue()))
//				((BaseResource) object).setOrgId(new StringType(HttpUtils.getHeader(HttpHeaders.ORG_ID)));
//			if (((BaseResource) object).getOrgCode() == null || ((BaseResource) object).getOrgCode() != null
//					&& StringUtils.isBlank(((BaseResource) object).getOrgCode().asStringValue()))
//				((BaseResource) object).setOrgCode(new StringType(HttpUtils.getHeader(HttpHeaders.ORG_CODE)));
		}
	}



}
