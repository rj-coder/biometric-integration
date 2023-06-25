package in.westerncoal.biometric.service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import in.westerncoal.biometric.model.Attendance;

@Component
public interface AttendanceService {

	Attendance saveAttendance(Attendance attendance);

	List<Attendance> saveAttendances(List<Attendance> attendances);

	void saveNewAttendances(List<Attendance> attendances);
	
	Date findMaxDateFromBiometricMachine(@Param("terminalId") String terminalId);


}
