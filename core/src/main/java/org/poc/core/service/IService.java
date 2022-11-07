/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.poc.core.service;

import java.io.Serializable;
import java.util.List;

import org.poc.core.model.ResourceFailureRecovery;
import com.google.gson.JsonObject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author LENOVO PC
 */
public interface IService<T>  {

    List<T> findAll();

    <S extends T> S findById(String id);
    List<ResourceFailureRecovery> findAllFailedData();
    ResourceFailureRecovery findFailedDataById(String id);
    List<ResourceFailureRecovery> searchFailedData(JsonObject search);
    void processFailedDataById(String id);
    void setDao(CrudRepository dao);

    <S extends T> S save(S object);

    <S extends T> List<S> saveAll(List<S> object);

    List<T> search(String column, Object value);

    List<T> goToPage(int pageNo);

    Page<T> searchPageable(String column, Object value, Pageable pageable);

    List<T> search(String column, Object value, Object value2);

    Page<T> search(JsonObject object, Pageable pageable);

    List<T> search(JsonObject object);

    Page<T> searchPageable(String column, Object value, Object value2, Pageable pageable);
    
    T create(T object);

    T update(T object,String id);

    T patch(T objectTopatch,String id) ;

    void  deleteById(Serializable objId);

    void delete(T obj);
    
    void validateEntity(T object) ;

    void setBusinessClass(Class cls);
    
}
