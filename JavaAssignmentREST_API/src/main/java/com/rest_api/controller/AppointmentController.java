package com.rest_api.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rest_api.entity.Appointment;
import com.rest_api.service.AppointmentService;

@RestController
@RequestMapping("/appoint")
public class AppointmentController {
	
	@Autowired
	private AppointmentService service;

	@PostMapping("/add")
	public ResponseEntity<String> appoint(@Valid @RequestBody Appointment appointment,BindingResult result){
		
		String errors="";
		int i=1;
		
		if(result.hasErrors()) {			
			List<ObjectError> allErrors = result.getAllErrors();
			for(ObjectError error:allErrors) {
				errors=errors+"Error "+i+" : "+error.getDefaultMessage()+"\n";
				i++;
			}
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errors);
		}
		
		String msg=service.appoint(appointment);
		if(msg.startsWith("Appointment"))
			return ResponseEntity.ok().body(msg);
		else if(msg.startsWith("Please"))
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
	}
	
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<String> deleteAppoint(@PathVariable("id") int id){
		
		if(service.delete_appoint(id))
			return ResponseEntity.ok("Appointment Deleted Successfully");
		else
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error 1 : Please Check The Id Or Try Again Later");
		
	}
	
}
