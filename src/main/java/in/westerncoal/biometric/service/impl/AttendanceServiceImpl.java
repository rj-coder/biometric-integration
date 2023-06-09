package in.westerncoal.biometric.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import in.westerncoal.biometric.model.Attendance;
import in.westerncoal.biometric.repository.AttendanceRepository;
import in.westerncoal.biometric.service.AttendanceService;

@Service
public class AttendanceServiceImpl implements AttendanceService{
	@Autowired
	AttendanceRepository attendancerepository;

	public Attendance saveAttendance(Attendance attendance) {
		return attendancerepository.save(attendance);
	}

	public List<Attendance> saveAttendances(List<Attendance> attendances) {
		return attendancerepository.saveAll(attendances);
	}
	
	 
}
