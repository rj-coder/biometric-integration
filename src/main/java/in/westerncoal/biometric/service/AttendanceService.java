package in.westerncoal.biometric.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import in.westerncoal.biometric.model.Attendance;
import in.westerncoal.biometric.repository.AttendanceRepository;

@Component
public interface AttendanceService {

	Attendance saveAttendance(Attendance attendance);

	List<Attendance> saveAttendances(List<Attendance> attendances);

}
