package onlineDoctorAppointmentSystemBackend.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlineDoctorAppointmentSystemBackend.app.custom_exception.ResourseNotFoundException;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Appointment;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Doctor;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Patient;
import onlineDoctorAppointmentSystemBackend.app.repository.AppointmentRepository;
import onlineDoctorAppointmentSystemBackend.app.service.intf.EmailSenderServiceIntf;
import onlineDoctorAppointmentSystemBackend.app.service.intf.PatientServiceIntf;

@Service
@Transactional
public class EmailSenderService implements EmailSenderServiceIntf{

	@Autowired
	private PatientServiceIntf patientService;
	
	@Autowired
	private AppointmentRepository appointmentRepo;
	
	@Autowired
	private JavaMailSender mailSender;
	
	public void sendSimpleEmail(String toEmail, String body, String subject) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("aonlinedoctor@gmail.com");
		message.setTo(toEmail);
		message.setText(body);
		message.setSubject(subject);
		
		mailSender.send(message);
		System.out.println("message send....");
		
	}
	
	@Override
	public void sendEmailOnAppointmentBooking(Long patientId,String time) {
		Patient patient = patientService.getPatientDetails(patientId);
		sendSimpleEmail(patient.getEmail(), 
				"Your appointment has been booked at "+time,
				"Appointment Confirmation");
	}
	
	@Override
	public void sendEmailTokenToResetPassword(String userEmail, Long token) 
	{
		sendSimpleEmail(userEmail, 
				"Token to reset your password : "+token,
				"Reset Password");
	}
	
	@Override
	public void sendEmailOnCancelAppointment(Long appointmentId) {
	
		Appointment appointment = appointmentRepo.findById(appointmentId).orElseThrow(() -> new ResourseNotFoundException("Invalid Appointment id!!!"));
		Doctor doctor = appointment.getDoctor();
		Patient patient = appointment.getPatient();
		
		sendSimpleEmail(patient.getEmail(), 
				"Your appointment has been cancelled with doctor : "+doctor.getFirstName(),
				"Appointment Cancelled");
		
		sendSimpleEmail(doctor.getEmail(), 
				"Appointment with patient : "+patient.getFirstName()+" has been cancelled",
				"Appointment Cancelled");
	}
	
	
	
}
