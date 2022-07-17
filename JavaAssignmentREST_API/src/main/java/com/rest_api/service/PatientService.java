package com.rest_api.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rest_api.dao.AppointmentRepository;
import com.rest_api.dao.DoctorRepository;
import com.rest_api.dao.PatientRepository;
import com.rest_api.entity.Appointment;
import com.rest_api.entity.Doctor;
import com.rest_api.entity.Patient;

@Service
public class PatientService {
	
	@Autowired
	private PatientRepository repo;
	
	@Autowired
	private AppointmentRepository repo1;
	
	@Autowired
	private DoctorRepository repo2;

	public Patient add_Patient(Patient patient) {
				
		Patient pat=null;
		try {
			patient.setCity(patient.getCity().toUpperCase());
			patient.setSymptom(patient.getSymptom().toUpperCase());
			pat = repo.save(patient);
		}catch(Exception e) {}
		return pat;
		
	}
	
	public boolean delete_patient(int id) {
		
		try {
			repo1.deleteByPatId(id);
		}catch(Exception e) {}
		
		try {
			repo.deleteById(id);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}
	
	public List<Patient> get_patients(){
				
		List<Patient> patients=new ArrayList<>();
		try {
			patients = (List<Patient>)repo.findAll();	
			for(Patient p:patients) {
				List<Appointment> appointments= repo1.findByPatId(p.getId());
				for(Appointment a:appointments) {
					Optional<Doctor> opt = repo2.findById(a.getDocId());
					Doctor d=opt.get();
					HashMap<String,Object> details=new HashMap<>();
					details.put("Doctor_Name", d.getName());
					details.put("Doctor_Email", d.getEmail());
					details.put("Doctor_Phone", d.getPhone());
					details.put("Doctor_Speciality", d.getSpeciality());
					a.setDetails(details);
				}
				p.setAppointment(appointments);
			}
		}catch(Exception e) {
		}
		return patients;
		
	}
	
	public Patient get_patient(int id){
		
		try {
			Optional<Patient> opt = repo.findById(id);	
			Patient patient=opt.get();
			List<Appointment> appointments= repo1.findByPatId(patient.getId());
			for(Appointment a:appointments) {
				Optional<Doctor> opt1 = repo2.findById(a.getDocId());
				Doctor d=opt1.get();
				HashMap<String,Object> details=new HashMap<>();
				details.put("Doctor_Name", d.getName());
				details.put("Doctor_Email", d.getEmail());
				details.put("Doctor_Phone", d.getPhone());
				details.put("Doctor_Speciality", d.getSpeciality());
				a.setDetails(details);
			}

			patient.setAppointment(appointments);
			return patient;
		}catch(Exception e) {
			return null;
		}
		
	}

}
