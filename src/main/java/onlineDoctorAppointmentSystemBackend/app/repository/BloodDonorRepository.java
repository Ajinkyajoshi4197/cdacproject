package onlineDoctorAppointmentSystemBackend.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import onlineDoctorAppointmentSystemBackend.app.entity.model.BloodDonor;
import onlineDoctorAppointmentSystemBackend.app.entity.model.BloodGroup;

@Repository
public interface BloodDonorRepository extends JpaRepository<BloodDonor, Long>{
	
	List<BloodDonor> findByCityAndBloodGroup(String city, BloodGroup bloodGroup);
}
