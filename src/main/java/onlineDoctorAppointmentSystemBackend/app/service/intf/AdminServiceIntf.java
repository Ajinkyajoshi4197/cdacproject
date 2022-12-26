package onlineDoctorAppointmentSystemBackend.app.service.intf;

import java.util.List;

import onlineDoctorAppointmentSystemBackend.app.entity.model.Admin;

public interface AdminServiceIntf {

	//get admin list
	List<Admin> getListOfAdmin();
	
}
