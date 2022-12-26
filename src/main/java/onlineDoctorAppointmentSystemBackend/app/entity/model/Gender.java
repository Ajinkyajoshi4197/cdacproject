package onlineDoctorAppointmentSystemBackend.app.entity.model;

public enum Gender {
	MALE, FEMALE, OTHER;
	
	@Override
	public String toString() {
		return name();
	}
	
}
