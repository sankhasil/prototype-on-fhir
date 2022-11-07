package org.poc.core.base.rest.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.poc.core.constants.HttpHeaders;
import org.poc.core.model.AbstractBaseEntity;
import org.hl7.fhir.dstu3.model.BaseResource;
import org.hl7.fhir.dstu3.model.StringType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.gson.JsonObject;

/**
 * 
 * @author Jawath,Sankha
 *
 */
public final class HttpUtils {

	private static final String USER_ID = "userId";
	private static final String USER_NAME = "userName";
	private static final String ORG_CODE = "orgCode";
	private static final String ORG_ID = "orgId";
	private static final String UNIT_CODE = "unitCode";
	private static final String UNIT_ID = "unitId";
	private static final String ACTION = "action";
	private static final String ROLE = "role";


	private HttpUtils() {

	}

	public static String getHeader(String header) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return request.getHeader(header);
		}
		return null;
	}

	public static String getHeader(HttpHeaders header) {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes instanceof ServletRequestAttributes) {
			HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
			return request.getHeader(header.getValue());
		}
		return null;
	}

	public static void setHeaderToPayloadByKey(String header, Object object, String attribute) {
		// reflection set object attribute
		String headerVal = getHeader(header);
		if (object instanceof JsonObject) {
			JsonObject payLoadJson = (JsonObject) object;
			payLoadJson.addProperty(attribute, headerVal);
		}
	}

	public static void setHeadersToRequestPayload(JsonObject payload) {
		payload.addProperty(UNIT_ID, getHeader(HttpHeaders.UNIT_ID.getValue()));
		payload.addProperty(UNIT_CODE, getHeader(HttpHeaders.UNIT_CODE.getValue()));
		payload.addProperty(ORG_ID, getHeader(HttpHeaders.ORG_ID.getValue()));
		payload.addProperty(ORG_CODE, getHeader(HttpHeaders.ORG_CODE.getValue()));
		payload.addProperty(USER_NAME, getHeader(HttpHeaders.USER_NAME.getValue()));
		payload.addProperty(USER_ID, getHeader(HttpHeaders.USER_ID.getValue()));
		payload.addProperty(ACTION, getHeader(HttpHeaders.ACTION.getValue()));
		payload.addProperty(ROLE, getHeader(HttpHeaders.ROLE.getValue()));	}

	public static void setHeadersToPayloadForSave(Object object) {
		if (object instanceof BaseResource) {
//			if (((BaseResource) object).getUnitId() == null || (((BaseResource) object).getUnitId() != null
//					&& StringUtils.isBlank(((BaseResource) object).getUnitId().asStringValue())))
//				((BaseResource) object).setUnitId(new StringType(getHeader(HttpHeaders.UNIT_ID)));
//			if (((BaseResource) object).getUnitCode() == null || ((BaseResource) object).getUnitCode() != null
//					&& StringUtils.isBlank(((BaseResource) object).getUnitCode().asStringValue()))
//				((BaseResource) object).setUnitCode(new StringType(getHeader(HttpHeaders.UNIT_CODE)));
//			if (((BaseResource) object).getOrgId() == null || ((BaseResource) object).getOrgId() != null
//					&& StringUtils.isBlank(((BaseResource) object).getOrgId().asStringValue()))
//				((BaseResource) object).setOrgId(new StringType(getHeader(HttpHeaders.ORG_ID)));
//			if (((BaseResource) object).getOrgCode() == null || ((BaseResource) object).getOrgCode() != null
//					&& StringUtils.isBlank(((BaseResource) object).getOrgCode().asStringValue()))
//				((BaseResource) object).setOrgCode(new StringType(getHeader(HttpHeaders.ORG_CODE)));
		}
		
		else if (object instanceof AbstractBaseEntity) {
			if (((AbstractBaseEntity) object).getUnitCode() == null || ((AbstractBaseEntity) object).getUnitCode() != null
					&& StringUtils.isBlank(((AbstractBaseEntity) object).getUnitCode()))
				((AbstractBaseEntity) object).setUnitCode(getHeader(HttpHeaders.UNIT_CODE));
			if (((AbstractBaseEntity) object).getOrgCode() == null || ((AbstractBaseEntity) object).getOrgCode() != null
					&& StringUtils.isBlank(((AbstractBaseEntity) object).getOrgCode()))
				((AbstractBaseEntity) object).setOrgCode(getHeader(HttpHeaders.ORG_CODE));
			
			
		}
	}

}
