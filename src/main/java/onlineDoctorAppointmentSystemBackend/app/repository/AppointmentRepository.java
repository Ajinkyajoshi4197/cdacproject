package onlineDoctorAppointmentSystemBackend.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import onlineDoctorAppointmentSystemBackend.app.entity.model.Appointment;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Doctor;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Patient;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long>{

	List<Appointment> findByPatientAndAppointmentTimeAfter(Patient patient, LocalDateTime time);
	
	List<Appointment> findByPatientAndAppointmentTimeBeforeOrderByAppointmentTimeDesc(Patient patient, LocalDateTime time);
	
	List<Appointment> findByDoctorAndAppointmentTimeAfter(Doctor doctor, LocalDateTime time);
		
	List<Appointment> findByDoctorAndPatientAndAppointmentTimeBeforeOrderByAppointmentTimeDesc(Doctor doctor, Patient patient, LocalDateTime time);
	
	List<Appointment> findByDoctorAndAppointmentTimeBeforeOrderByAppointmentTimeDesc(Doctor doctor, LocalDateTime time);

	@Query("SELECT a.id FROM Appointment a WHERE a.patient.id =?1")	
	List<Long> getAppointmentIdListForPatient(Long patientId);	
	
		
}
