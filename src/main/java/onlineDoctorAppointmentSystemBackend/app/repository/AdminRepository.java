package onlineDoctorAppointmentSystemBackend.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import onlineDoctorAppointmentSystemBackend.app.entity.model.Admin;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

	Optional<Admin> findByEmailAndPassword(String email, String password);

	Optional<Admin> findByEmail(String email);
	
}
