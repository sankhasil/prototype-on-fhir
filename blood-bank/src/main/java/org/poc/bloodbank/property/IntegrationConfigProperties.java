
package org.poc.bloodbank.property;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Sankha Sil
 */

@ConfigurationProperties(prefix = "integration")
public class IntegrationConfigProperties {

    @NotNull
    private String ruleEngineServiceID;
    @NotNull
    private String opdServiceID;
    @NotNull
    private String schedulerServiceID;
    @NotNull
    private String auditTrailServiceID;
    @NotNull
    private String mastersServiceID;
    @NotNull
    private String mirthBaseUrlServiceRequest;
    @NotNull
    private String emailServiceId;

    public String getMastersServiceID() {
        return mastersServiceID;
    }

    public void setMastersServiceID(String mastersServiceID) {
        this.mastersServiceID = mastersServiceID;
    }

    public String getAuditTrailServiceID() {
        return auditTrailServiceID;
    }

    public void setAuditTrailServiceID(String auditTrailServiceID) {
        this.auditTrailServiceID = auditTrailServiceID;
    }

    public String getSchedulerServiceID() {
        return schedulerServiceID;
    }

    public void setSchedulerServiceID(String schedulerServiceID) {
        this.schedulerServiceID = schedulerServiceID;
    }

    public String getOpdServiceID() {
        return opdServiceID;
    }

    public void setOpdServiceID(String opdServiceID) {
        this.opdServiceID = opdServiceID;
    }

    public String getRuleEngineServiceID() {
        return ruleEngineServiceID;
    }

    public void setRuleEngineServiceID(String ruleEngineServiceID) {
        this.ruleEngineServiceID = ruleEngineServiceID;
    }

    public String getMirthBaseUrlServiceRequest() {
        return mirthBaseUrlServiceRequest;
    }

    public void setMirthBaseUrlServiceRequest(String mirthBaseUrlServiceRequest) {
        this.mirthBaseUrlServiceRequest = mirthBaseUrlServiceRequest;
    }

    public String getEmailServiceId() {
        return emailServiceId;
    }

    public void setEmailServiceId(String emailServiceId) {
        this.emailServiceId = emailServiceId;
    }
}
