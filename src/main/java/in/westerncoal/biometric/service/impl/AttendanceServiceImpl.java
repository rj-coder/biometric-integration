package in.westerncoal.biometric.service.impl;

import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
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
		Session session = em.unwrap(Session.class);

		// Create and configure the batch
		session.setJdbcBatchSize(attendances.size());

		// Execute batch operations
		for (Attendance attendance : attendances)
			session.save(attendance);
			

		// Flush and clear any remaining entities in the batch
		session.flush();
		session.clear();
	}

	public Date findMaxDateFromBiometricMachine(String terminalId) {
		return attendancerepository.findMaxDateFromBiometricMachine(terminalId);
	}

}
