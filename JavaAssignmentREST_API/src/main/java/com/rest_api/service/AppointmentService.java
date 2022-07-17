package com.rest_api.service;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class AppointmentService {
	
	@Autowired
	private AppointmentRepository repo;
	
	@Autowired
	private PatientRepository repo1;
	
	@Autowired
	private DoctorRepository repo2;
	
	public String appoint(Appointment appointment) {
		
		//Checking For Existence Of Patient
		try {
			Optional<Patient> opt=repo1.findById(appointment.getPatId());
			Patient pat=opt.get();		
		}catch(Exception e) { return "Patient Does Not Exist"; }

		//Checking For Existence Of Doctor		
		try {
			Optional<Doctor> opt1=repo2.findById(appointment.getDocId());
			Doctor doc=opt1.get();			
		}catch(Exception e) { return "Doctor Does Not Exist"; }
		
		//Checking Time Range		
		DateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
		Date time=null;
		try {
			time = dateFormat.parse(appointment.getAppointTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		Date date1=null;
		Date date2=null;
		String date_1="10:59 AM";
		String date_2="05:01 PM";
		try {
			date1 = dateFormat.parse(date_1);
			date2 = dateFormat.parse(date_2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		if(!(time.after(date1) && time.before(date2))) {
			return "Doctor is available from Monday to Saturday from 11 am to 5 pm, format('hh:mm AM/PM')";
		}
		
		//Checking Monday To Saturday
		try {
			Date sdf = new SimpleDateFormat("dd/MM/yyyy").parse(appointment.getAppointDate());
			Format f = new SimpleDateFormat("EEEE");
			String str = f.format(sdf);
			if(str.equals("Sunday"))
				return "Doctor is available from Monday to Saturday from 11 am to 5 pm, format('hh:mm AM/PM')";
		} catch (ParseException e) {
			e.printStackTrace();
		}   

		//Incrementing Time
		int flag=0;
		String increment_time="";
		String increment_time1="";
		if(appointment.getAppointTime().startsWith("12")) {
			increment_time="13"+appointment.getAppointTime().substring(2);
			increment_time1="01"+appointment.getAppointTime().substring(2);
			flag=1;
		}
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(time);
			calendar.add(Calendar.HOUR, 1);
			time = calendar.getTime();
			increment_time=dateFormat.format(time);
		}
	
		//Saving Appointment If Possible
		Appointment appoint=null;
		List<Appointment> appointments=repo.findByDocIdAndAppointDate(appointment.getDocId(), appointment.getAppointDate());
		if(appointments.size()>0){
			List<Appointment> appointments1;
			if(flag==1)
				appointments1=repo.findByDocIdAndAppointDateAndAppointTimeBetweenOrAppointTimeBetween(appointment.getDocId(), appointment.getAppointDate(), appointment.getAppointTime(), increment_time, "01:00 PM", increment_time1);
			else
				appointments1=repo.findByDocIdAndAppointDateAndAppointTimeBetween(appointment.getDocId(), appointment.getAppointDate(), appointment.getAppointTime(), increment_time);
			if(appointments1.size()>0)
				return "The doctor already have another appointment at the same time";
		}
		appoint = repo.save(appointment);			
		if(appoint==null)
			return "Please Try Again Later";
		return "Appointment Appointed Successfully";
			
	}
	
	public boolean delete_appoint(int id) {
		
		try {
			repo.deleteById(id);
			return true;
		}catch(Exception e) {
			return false;
		}
		
	}

}
