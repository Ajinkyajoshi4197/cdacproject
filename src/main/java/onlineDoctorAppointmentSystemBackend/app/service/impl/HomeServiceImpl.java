package onlineDoctorAppointmentSystemBackend.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlineDoctorAppointmentSystemBackend.app.custom_exception.ResourseNotFoundException;
import onlineDoctorAppointmentSystemBackend.app.dto.LoginResponse;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Admin;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Doctor;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Patient;
import onlineDoctorAppointmentSystemBackend.app.repository.AdminRepository;
import onlineDoctorAppointmentSystemBackend.app.repository.DoctorRepository;
import onlineDoctorAppointmentSystemBackend.app.repository.PatientRepository;
import onlineDoctorAppointmentSystemBackend.app.service.intf.HomeServiceIntf;

@Service
@Transactional
public class HomeServiceImpl implements HomeServiceIntf {

	private AdminRepository adminRepo;

	private PatientRepository patientRepo;

	private PasswordEncoder passwordEncoder;

	private DoctorRepository doctorRepo;

	private EmailSenderService emailSenderService;
	

	// constructor level dependencies
	@Autowired
	public HomeServiceImpl(AdminRepository adminRepo, @Autowired PatientRepository patientRepo,
			PasswordEncoder passwordEncoder, DoctorRepository doctorRepo, EmailSenderService emailSenderService) {

		this.adminRepo = adminRepo;
		this.patientRepo = patientRepo;
		this.passwordEncoder = passwordEncoder;
		this.doctorRepo = doctorRepo;
		this.emailSenderService = emailSenderService;
	}

	@Override
	public LoginResponse authenticateUser(String email, String password) {

		try {
			Admin admin = adminRepo.findByEmail(email)
					.orElseThrow(() -> new ResourseNotFoundException("User with email: " + email + " doesn't exist."));
			if (password.equals(admin.getPassword())) {
				return new LoginResponse(admin.getId(), admin.getName(), "admin");
			}
		} catch (Exception e) {
			System.out.println("msg : " + e.getMessage());
		}

		try {
			Patient patient = patientRepo.findByEmail(email)
					.orElseThrow(() -> new ResourseNotFoundException("User with email: " + email + " doesn't exist."));
			if (passwordEncoder.matches(password, patient.getPassword())) {
				return new LoginResponse(patient.getId(), patient.getFirstName(), "patient");
			}

		} catch (Exception e) {
			System.out.println("msg : " + e.getMessage());
		}

		try {
			Doctor doctor = doctorRepo.findByEmail(email)
					.orElseThrow(() -> new ResourseNotFoundException("User with email: " + email + " doesn't exist."));
			if (passwordEncoder.matches(password, doctor.getPassword())) {
				return new LoginResponse(doctor.getId(), doctor.getFirstName(), "doctor");
			}

		} catch (Exception e) {
			System.out.println("msg : " + e.getMessage());
		}
		throw new ResourseNotFoundException("Invalid Email or Password!!!");
	}

	@Override
	public Long generateToken(String email) {

		boolean flag = false;

		try {
			patientRepo.findByEmail(email).get();
			flag = true;
		} catch (Exception e) {
			System.out.println("Patient Err : " + e);
		}

		try {
			adminRepo.findByEmail(email).get();
			flag = true;
		} catch (Exception e) {
			System.out.println("Admin Err : " + e);
		}
		try {
			doctorRepo.findByEmail(email).get();
			flag = true;
		} catch (Exception e) {
			System.out.println("Doctor Err : " + e);
		}

		Long token = null;
		int min = 0, max = 0;

		if (flag == true) {
			min = 1000;
			max = 9999;
			token = (long) (Math.random() * (max - min + 1) + min);
			System.out.println("**** TOKEN : "+token);
			emailSenderService.sendEmailTokenToResetPassword(email, token);

		} else {
			throw new ResourseNotFoundException("Invalid Email!!!");
		}

		return token;
	}

	@Override
	public int resetPassword(String userEmail, String userNewPassword) {
		
		try {
			Patient patient = patientRepo.findByEmail(userEmail).get();
			if(passwordEncoder.matches(userNewPassword, patient.getPassword())) {
				return 1;
			}
			patient.setPassword(passwordEncoder.encode(userNewPassword));
			return 0;
			
		} catch (Exception e) {
			System.out.println("Patient Err : " + e);
		}

		try {
			
			Doctor doctor = doctorRepo.findByEmail(userEmail).get();
			if(passwordEncoder.matches(userNewPassword, doctor.getPassword())) {
				return 1;
			}
			doctor.setPassword(passwordEncoder.encode(userNewPassword));
			return 0;
		} catch (Exception e) {
			System.out.println("Doctor Err : " + e);
		}
		
		return 1;
	}

}