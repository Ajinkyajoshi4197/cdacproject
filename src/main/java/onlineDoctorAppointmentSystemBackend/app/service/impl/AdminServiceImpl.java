package onlineDoctorAppointmentSystemBackend.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import onlineDoctorAppointmentSystemBackend.app.entity.model.Admin;
import onlineDoctorAppointmentSystemBackend.app.repository.AdminRepository;
import onlineDoctorAppointmentSystemBackend.app.service.intf.AdminServiceIntf;

@Service
@Transactional
public class AdminServiceImpl implements AdminServiceIntf {

	@Autowired
	private AdminRepository adminRepo;
	
	@Override
	public List<Admin> getListOfAdmin() {
		return adminRepo.findAll();
	}

}
