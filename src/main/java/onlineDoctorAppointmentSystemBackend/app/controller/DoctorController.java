package onlineDoctorAppointmentSystemBackend.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import onlineDoctorAppointmentSystemBackend.app.dto.DoctorDTO;
import onlineDoctorAppointmentSystemBackend.app.entity.model.DoctorTimeTable;
import onlineDoctorAppointmentSystemBackend.app.service.intf.DoctorServiceIntf;

@RestController
@RequestMapping("/doctor")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DoctorController {

	// Dependencies injection
	private DoctorServiceIntf doctorService;
	
	@Autowired // constructor level autowiring
	public DoctorController(DoctorServiceIntf doctorService) {
		this.doctorService = doctorService;
	}
	
	@PostMapping("/createAppointmentSlot/{doctorId}")
	public List<LocalDateTime> createAppointmentSlots(@PathVariable Long doctorId,
			@RequestBody DoctorTimeTable doctorTimeTable) {
		return doctorService.createAvailableSlotsDetails(doctorId, doctorTimeTable);
	}

	@GetMapping("/getDoctorDetails/{doctorId}")
	public ResponseEntity<?> getDoctorDetails(@PathVariable Long doctorId) {
		return ResponseEntity.ok(doctorService.getDoctorDetails(doctorId));
	}

	@PutMapping("/updateDoctor/{doctorId}")
	public ResponseEntity<?> updateDoctorDetails(@RequestBody DoctorDTO detachedDoctor, @PathVariable Long doctorId) {
		return ResponseEntity.ok(doctorService.updateDoctorDetails(detachedDoctor, doctorId));
	}

}