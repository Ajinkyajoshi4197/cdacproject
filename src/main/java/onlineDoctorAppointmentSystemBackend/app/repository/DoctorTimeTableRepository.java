package onlineDoctorAppointmentSystemBackend.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import onlineDoctorAppointmentSystemBackend.app.entity.model.DoctorTimeTable;

public interface DoctorTimeTableRepository extends JpaRepository<DoctorTimeTable, Long> {

}
