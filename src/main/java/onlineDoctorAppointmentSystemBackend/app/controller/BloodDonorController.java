package onlineDoctorAppointmentSystemBackend.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import onlineDoctorAppointmentSystemBackend.app.entity.model.BloodDonor;
import onlineDoctorAppointmentSystemBackend.app.service.intf.BloodDonorIntf;

@RestController
@RequestMapping("/blood_donation")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class BloodDonorController {

	// Dependencies injection 
	private BloodDonorIntf bloodDonorService;
	
	@Autowired // constructor level autowiring
	public BloodDonorController(BloodDonorIntf bloodDonorService) {
		this.bloodDonorService = bloodDonorService;
	}

	@GetMapping("/search/{city}/{bloodGroup}")
	public List<BloodDonor> getAllBloodDonorsByCityAndBloodGroup(@PathVariable String city,
			@PathVariable String bloodGroup) {
		return bloodDonorService.getAllBloodDonorsByCityAndBloodGroup(city, bloodGroup);
	}
}
