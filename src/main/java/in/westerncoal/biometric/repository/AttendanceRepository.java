package in.westerncoal.biometric.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.westerncoal.biometric.model.Attendance;
import in.westerncoal.biometric.model.AttendanceKey;

 
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceKey> {
	
}
 