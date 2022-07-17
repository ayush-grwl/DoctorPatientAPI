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
public class DoctorService {
	
	@Autowired
	private DoctorRepository repo;
	
	@Autowired
	private AppointmentRepository repo1;
	
	@Autowired
	private PatientService service;
	
	@Autowired
	private PatientRepository repo2;
	
	public Doctor add_Doctor(Doctor doctor) {
		
		Doctor doc=null;
		try {
			doctor.setCity(doctor.getCity().toUpperCase());
			doctor.setSpeciality(doctor.getSpeciality().toUpperCase());
			doc = repo.save(doctor);
		}catch(Exception e) {}
		return doc;
		
	}
	
	public boolean delete_doctor(int id) {
		
		try {
			repo1.deleteByDocId(id);
		}catch(Exception e) {}
		
		try {			
			repo.deleteById(id);						
			return true;
		}catch(Exception e) {
			System.out.print(e.getMessage());
			return false;
		}
		
	}
	
	public List<Doctor> get_doctors(){
		
		List<Doctor> doctors=new ArrayList<>();
		try {
			doctors = (List<Doctor>)repo.findAll();
			for(Doctor d:doctors) {
				List<Appointment> appointments= repo1.findByDocId(d.getId());	
				for(Appointment a:appointments) {
					Optional<Patient> opt = repo2.findById(a.getPatId());
					Patient p=opt.get();
					HashMap<String,Object> details=new HashMap<>();
					details.put("Patient_Name", p.getName());
					details.put("Patient_Email", p.getEmail());
					details.put("Patient_Phone", p.getPhone());
					details.put("Patient_Symptom", p.getSymptom());
					a.setDetails(details);
				}
				d.setAppointment(appointments);
			}
		}catch(Exception e) {			
		}
		return doctors;
		
	}
	
	public Doctor get_doctor(int id){
		
		try {
			Optional<Doctor> opt = repo.findById(id);	
			Doctor doctor=opt.get();
			List<Appointment> appointments= repo1.findByDocId(doctor.getId());
			for(Appointment a:appointments) {
				Optional<Patient> opt1 = repo2.findById(a.getPatId());
				Patient p=opt1.get();
				HashMap<String,Object> details=new HashMap<>();
				details.put("Patient_Name", p.getName());
				details.put("Patient_Email", p.getEmail());
				details.put("Patient_Phone", p.getPhone());
				details.put("Patient_Symptom", p.getSymptom());
				a.setDetails(details);
			}
			doctor.setAppointment(appointments);
			return doctor;
		}catch(Exception e) {
			return null;
		}
		
	}
	
	public String getSpecialization(String symptom) {
		
		String doctor_spec="";
		if(symptom.equals("ARTHRITIS")||symptom.equals("BACKPAIN")||symptom.equals("TISSUE INJURIES"))
			doctor_spec="ORTHOPEDIC";
		
		else if(symptom.equals("DYSMENORRHEA"))
			doctor_spec="GYNECOLOGY";
		
		else if(symptom.equals("SKIN INFECTION")||symptom.equals("SKIN BURN"))
			doctor_spec="DERMATOLOGY";
		
		else
			doctor_spec="ENT SPECIALIST";
		return doctor_spec;
		
	}
	
	public List<Doctor> suggest_doctor(int id) {
		
		Patient patient=service.get_patient(id);
		String symptom=patient.getSymptom();
		String doctor_spec=getSpecialization(symptom);
		List<Doctor> doctors=repo.findBySpecialityAndCity(doctor_spec,patient.getCity());		
		return doctors;
		
	}
	
	public List<Doctor> get_doctor_city(int id) {
		
		List<Doctor> doctors=new ArrayList<>();			
		try {
			Patient patient=service.get_patient(id);	
			doctors=repo.findByCity(patient.getCity());
		}catch(Exception e) {}
		return doctors;
		
	}
	
	public List<Doctor> get_doctor_speciality(int id) {
		
		List<Doctor> doctors=new ArrayList<>();
		try {
			Patient patient=service.get_patient(id);
			String symptom=patient.getSymptom();
			String doctor_spec=getSpecialization(symptom);
			doctors=repo.findBySpeciality(doctor_spec);
		}catch(Exception e) {}
		return doctors;
		
	}
	
}
