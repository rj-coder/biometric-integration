package in.westerncoal.biometric.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
	public List<Attendance> saveNewAttendances(List<Attendance> attendances) {

		int batchSize = 50; // Set the desired batch size

	    for (int i = 0; i < attendances.size(); i++) {
	        em.merge(attendances.get(i));

	        if (i % batchSize == 0 && i > 0) {
	            em.flush();
	            em.clear();
	        }
	    }

	    return attendances;

	}

}
