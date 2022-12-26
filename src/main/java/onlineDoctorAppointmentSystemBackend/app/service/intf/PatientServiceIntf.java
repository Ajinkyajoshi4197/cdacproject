package onlineDoctorAppointmentSystemBackend.app.service.intf;

import java.util.List;

import onlineDoctorAppointmentSystemBackend.app.dto.PatientDTO;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Patient;

public interface PatientServiceIntf {
	
	//register new patient
	Patient savePatient(PatientDTO user);
	
	//delete patient
	String deletePatientById(Long patient_id);
	
	//getAll patients
	List<Patient> getAllPatients();

	//get specific patient
	Patient getPatientDetails(Long id);
	
	//update specific patient
	Patient updatePatientDetails(PatientDTO patientDTO, long id);

		
}
