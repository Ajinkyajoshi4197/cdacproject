package onlineDoctorAppointmentSystemBackend.app.service.intf;

import java.time.LocalDateTime;
import java.util.List;

import onlineDoctorAppointmentSystemBackend.app.entity.model.Appointment;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Doctor;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Patient;

public interface AppointmentServiceIntf {
	//get all appointment slots
	List<LocalDateTime> getAllAppointmentSlots(Long doctorId);
	
	//cancel appointment
	String cancelAppointment(Long appointmentId);

	//get list of all current appointments of particular patient
	List<Appointment> getAllPatientCurrentAppoitments(Long patientId);
	
	//get list of appointment history of patient
	List<Appointment> getAllPatientAppoitmentsHistory(Long patientId);

	//get list of all current appointments for doctor
	List<Appointment> getAllCurrentAppoitmentsForDoctor(Long doctorId);
	
	//get appointment history of patient for doctor
	List<Appointment> getPatientAppoitmentsHistoryForDoctor(Long doctorId, Long patientId);
	
	//get list of all appointment history for doctor
	List<Appointment> getAllAppoitmentsHistoryForDoctor(Long doctorId);

	//book an appointment
	List<LocalDateTime> bookAppointmentForPatient(Long doctorId, Long patientId, String time);
	
	//get patient by appointment id
	Patient getPatientByAppointmentId(Long appointmentId);

	Doctor getDoctorByAppointmentId(Long appointmentId);
	
}
