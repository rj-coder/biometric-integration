package in.westerncoal.biometric.repository;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.westerncoal.biometric.model.Attendance;
import in.westerncoal.biometric.model.AttendanceKey;

public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceKey> {

	@Query("SELECT MAX(a.AttendanceKey.punchDateTime) FROM Attendance a where a.terminalId = :terminalId")
	Date findMaxDateFromBiometricMachine(@Param("terminalId") String terminalId);
}
