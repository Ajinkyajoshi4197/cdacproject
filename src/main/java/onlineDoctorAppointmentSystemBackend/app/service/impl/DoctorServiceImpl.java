package onlineDoctorAppointmentSystemBackend.app.service.impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import onlineDoctorAppointmentSystemBackend.app.custom_exception.ResourseNotFoundException;
import onlineDoctorAppointmentSystemBackend.app.dto.DoctorDTO;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Appointment;
import onlineDoctorAppointmentSystemBackend.app.entity.model.Doctor;
import onlineDoctorAppointmentSystemBackend.app.entity.model.DoctorTimeTable;
import onlineDoctorAppointmentSystemBackend.app.repository.AdminRepository;
import onlineDoctorAppointmentSystemBackend.app.repository.AppointmentRepository;
import onlineDoctorAppointmentSystemBackend.app.repository.DoctorRepository;
import onlineDoctorAppointmentSystemBackend.app.repository.DoctorTimeTableRepository;
import onlineDoctorAppointmentSystemBackend.app.repository.PatientRepository;
import onlineDoctorAppointmentSystemBackend.app.service.intf.DoctorServiceIntf;

@Service
@Transactional
public class DoctorServiceImpl implements DoctorServiceIntf {

	private DoctorRepository doctorRepo;

	private AppointmentRepository appointmentRepo;

	private DoctorTimeTableRepository doctorTimeTableRepo;

	private PasswordEncoder passwordEncoder;

	private PatientRepository patientRepo;
	
	private AdminRepository adminRepo;
	
	private ModelMapper modelMapper;

	@Autowired
	public DoctorServiceImpl(DoctorRepository doctorRepo, AppointmentRepository appointmentRepo,
			DoctorTimeTableRepository doctorTimeTableRepo,  PasswordEncoder passwordEncoder,
			 PatientRepository patientRepo,  AdminRepository adminRepo ,ModelMapper modelMapper
			 ) {

		this.doctorRepo = doctorRepo;
		this.appointmentRepo = appointmentRepo;
		this.doctorTimeTableRepo = doctorTimeTableRepo;
		this.passwordEncoder = passwordEncoder;
		this.patientRepo = patientRepo;
		this.adminRepo = adminRepo;
		this.modelMapper = modelMapper;
		
	}

	@Override
	public Doctor saveDoctor(DoctorDTO doctor) {

		String email = doctor.getEmail();
		try {
			patientRepo.findByEmail(email).get();	
			return null;
		} catch (Exception e) {
			System.out.println("PatientErr : "+e);
		}

		try {
			adminRepo.findByEmail(email).get();
			return null;
		} catch (Exception e) {
			System.out.println("AdminErr : "+e);
		}
		
		Doctor newDoctor = Doctor.createDoctor(doctor);
		newDoctor.setPassword(passwordEncoder.encode(newDoctor.getPassword()));
		//newDoctor.setPassword(newDoctor.getPassword()); Simple Password 
		return doctorRepo.save(newDoctor);
	
		
	}

	@Override
	public List<String> getSpecializationsByCity(String city) {
		return doctorRepo.getSpecializationsByCity(city); // get all unique specialization list of doctors
	}

	@Override
	public List<Doctor> getAllDoctorsBySpecializationAndCity(String specialization, String city) {
		List<Doctor> doctors = doctorRepo.findAllBySpecializationAndCity(specialization, city);
		return doctors;
	}

	@Override
	public Doctor updateDoctorDetails(DoctorDTO doctorDTO, Long id) {
		
		Doctor doctor = doctorRepo.findById(id).orElseThrow(() -> new ResourseNotFoundException("Invalid doctor id!!!!"));
		Doctor d1 = this.modelMapper.map(doctorDTO, Doctor.class);
		
		d1.setId(id);
		d1.setPassword(doctor.getPassword());
		d1.setTimeSlot(doctor.getTimeSlot());
		
		return this.doctorRepo.save(d1);
		
	}

	@Override
	public Doctor getDoctorDetails(Long doctorId) {
		Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new ResourseNotFoundException("Invalid doctor id!!!"));
		System.out.println("GET DR DETAILS : "+doctor);
		return doctor;
	}

	
	@Override
	public List<Doctor> getAllDoctors() {
		return doctorRepo.findAll();
	}

	@Override
	public String deleteDoctorById(Long doctorId) {
		doctorRepo.deleteById(doctorId);
		return "Successfully Deleted doctor with id : " + doctorId;
	}

	@Override
	public List<LocalDateTime> createAvailableSlotsDetails(Long doctorId, DoctorTimeTable appointmentSlot) {

		Doctor doctor = doctorRepo.findById(doctorId).orElseThrow(() -> new ResourseNotFoundException("Doctor not found...!!!"));
		Map<LocalDateTime, Boolean> slotMap = new HashMap<>();

		List<LocalDateTime> list = new ArrayList<>();

		LocalDate startDate = appointmentSlot.getStartDate();
		LocalDate endDate = appointmentSlot.getEndDate();
		LocalTime startTime = appointmentSlot.getStartTime();
		LocalTime endTime = appointmentSlot.getEndTime();
		LocalTime breakTime = appointmentSlot.getBreakTime();
		int slotDuration = appointmentSlot.getSlotDuration();

		List<String> holidays = new ArrayList<>(appointmentSlot.getHolidays());

		String str = "";

		for (int i = 0; i < holidays.size(); i++) {
			str = holidays.get(i);
			holidays.set(i, str.toUpperCase());
		}

		Period period = Period.between(startDate, endDate);
		// get count of total days [start to end]
		int days = period.getDays();

		days += 1; // last date is excluded so add 1 later

		int totalMinutes = (int) ChronoUnit.MINUTES.between(startTime, endTime);
		int slots = (totalMinutes / slotDuration);

		// get total count of slots
		int totalSlots = slots * days;

		long addDate = 0, addTime = 0;

		for (int i = 0; i < totalSlots; i++) {

			if (i % (slots) == 0 && i != 0) {
				addDate++;
				addTime = 0;
			}

			LocalDate date = startDate.plusDays(addDate);
			DayOfWeek dayOfWeek = date.getDayOfWeek();

			if (holidays.contains(dayOfWeek.toString())) {
				if (i == 0) {
					addDate++;
				}
				i--;
				totalSlots = totalSlots - slots;
				continue;
			}

			LocalTime t = startTime.plusMinutes(slotDuration * (addTime++));

			if (t.equals(breakTime)) {
				slotMap.put(LocalDateTime.of(startDate.plusDays(addDate), t), false);
			} else {
				slotMap.put(LocalDateTime.of(startDate.plusDays(addDate), t), true);
				list.add(LocalDateTime.of(startDate.plusDays(addDate), t));
			}
		}
		appointmentSlot.setAvailableSlots(slotMap);
		DoctorTimeTable timeTable = doctorTimeTableRepo.save(appointmentSlot);
		doctor.setTimeSlot(timeTable);
		return list;
	}

	public void makeSlotsAvailable(Long appoitmentId) {

		// get appointment from appointmentId
		Appointment appointment = appointmentRepo.findById(appoitmentId)
				.orElseThrow(() -> new ResourseNotFoundException("Invalid appointment Id..."));

		// get appointmentTime
		LocalDateTime time = appointment.getAppointmentTime();

		// get doctor data w.r.t appointmentId
		Doctor doctor = appointment.getDoctor();

		// get availableSlots list of doctor
		Map<LocalDateTime, Boolean> availableSlots = doctor.getTimeSlot().getAvailableSlots();

		// make that slot available again before deleting patient
		availableSlots.put(time, true);

	}

}