package in.westerncoal.biometric.service;

import java.util.List;
import org.springframework.stereotype.Component;
import in.westerncoal.biometric.model.Attendance;

@Component
public interface AttendanceService {

	Attendance saveAttendance(Attendance attendance);

	List<Attendance> saveAttendances(List<Attendance> attendances);

}
