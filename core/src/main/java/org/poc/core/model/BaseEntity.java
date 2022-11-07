/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.poc.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 *
 * @author LENOVO PC
 */
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public interface BaseEntity {

    public String getId() ;

    public void setId(String id);


}
