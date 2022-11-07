package org.poc.bloodbank.property;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "patient")
public class ServicecConfigProperties {
    @NotNull
    private String autogenaratorCode;

    @NotNull
    private String autogenaratorStartno;

    public String getAutogenaratorCode() {
        return autogenaratorCode;
    }

    public void setAutogenaratorCode(String autogenaratorCode) {
        this.autogenaratorCode = autogenaratorCode;
    }

    public String getAutogenaratorStartno() {
        return autogenaratorStartno;
    }

    public void setAutogenaratorStartno(String autogenaratorStartno) {
        this.autogenaratorStartno = autogenaratorStartno;
    }

}
