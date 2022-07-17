package com.rest_api.dao;

import org.springframework.data.repository.CrudRepository;

import com.rest_api.entity.Patient;

public interface PatientRepository extends CrudRepository<Patient, Integer>{
	
}
