package in.westerncoal.biometric.service.impl;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import in.westerncoal.biometric.model.Attendance;
import in.westerncoal.biometric.repository.AttendanceRepository;
import in.westerncoal.biometric.service.AttendanceService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class AttendanceServiceImpl implements AttendanceService {
	@Autowired
	AttendanceRepository attendancerepository;

	@PersistenceContext
	EntityManager em;

	public Attendance saveAttendance(Attendance attendance) {
		return attendancerepository.save(attendance);
	}

	@Transactional
	public List<Attendance> saveAttendances(List<Attendance> attendances) {
		return attendancerepository.saveAll(attendances);
	}

	@Transactional
	public void saveNewAttendances(List<Attendance> attendances) {
		em.persist(attendances);
	}

	public Date findMaxDateFromBiometricMachine(String terminalId) {
		return attendancerepository.findMaxDateFromBiometricMachine(terminalId);
	}

}
