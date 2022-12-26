package onlineDoctorAppointmentSystemBackend.app.service.impl;

import java.util.List;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import onlineDoctorAppointmentSystemBackend.app.custom_exception.ResourseNotFoundException;
import onlineDoctorAppointmentSystemBackend.app.dto.PatientDTO;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Patient;
import onlineDoctorAppointmentSystemBackend.app.repository.AdminRepository;
import onlineDoctorAppointmentSystemBackend.app.repository.AppointmentRepository;
import onlineDoctorAppointmentSystemBackend.app.repository.DoctorRepository;
import onlineDoctorAppointmentSystemBackend.app.repository.PatientRepository;
import onlineDoctorAppointmentSystemBackend.app.service.intf.PatientServiceIntf;

@Service
@Transactional
public class PatientServiceImpl implements PatientServiceIntf {

	private PatientRepository patientRepo;

	private AdminRepository adminRepo;

	private DoctorRepository doctorRepo;

	private AppointmentRepository appointmentRepo;

	private DoctorServiceImpl doctorService;

	private PasswordEncoder passwordEncoder;
	
	private ModelMapper modelMapper;


	@Autowired
	public PatientServiceImpl(PatientRepository patientRepo, AppointmentRepository appointmentRepo,
			PasswordEncoder passwordEncoder, DoctorServiceImpl doctorService, AdminRepository adminRepo,
			DoctorRepository doctorRepo, ModelMapper modelMapper) {

		this.patientRepo = patientRepo;
		this.appointmentRepo = appointmentRepo;
		this.passwordEncoder = passwordEncoder;
		this.doctorService = doctorService;
		this.adminRepo = adminRepo;
		this.doctorRepo = doctorRepo;
		this.modelMapper = modelMapper;

	}

	public Patient savePatient(PatientDTO patient) {

		String email = patient.getEmail();
		try {
			doctorRepo.findByEmail(email).get();
			return null;
		} catch (Exception e) {
			System.out.println("DoctorErr : " + e);
		}

		try {
			adminRepo.findByEmail(email).get();
			return null;
		} catch (Exception e) {
			System.out.println("AdminErr : " + e);
		}

		Patient newPatient = Patient.createPatient(patient);
		newPatient.setPassword(passwordEncoder.encode(newPatient.getPassword()));
		//newPatient.setPassword(newPatient.getPassword()); Simple Password 
		return patientRepo.save(newPatient);
	}

	@Override
	public String deletePatientById(Long patient_id) {
		List<Long> appoitments = appointmentRepo.getAppointmentIdListForPatient(patient_id);
		appoitments.forEach(System.out::println);
		Long appointmentId = null;
		for (int i = 0; i < appoitments.size(); i++) {
			appointmentId = appoitments.get(0);
			doctorService.makeSlotsAvailable(appointmentId);
		}

		patientRepo.deleteById(patient_id);
		return "Successfully Deleted Patient with id : " + patient_id;
	}

	@Override
	public List<Patient> getAllPatients() {
		return patientRepo.findAll();
	}

	@Override
	public Patient getPatientDetails(Long id) {
		return patientRepo.findById(id).orElseThrow(() -> new ResourseNotFoundException("Invalid patient ID..."));
	}

	@Override
	public Patient updatePatientDetails(PatientDTO patientDTO, long id) {
		
		//System.out.println("Updated Value=>"+patientDTO);
		Patient patient = patientRepo.findById(id).orElseThrow(() -> new ResourseNotFoundException("Invalid Patient id!!!!"));
		Patient p1 = this.modelMapper.map(patientDTO, Patient.class);
		
		p1.setId(id);
		p1.setPassword(patient.getPassword());
		
		return this.patientRepo.save(p1);
		
	}

}
