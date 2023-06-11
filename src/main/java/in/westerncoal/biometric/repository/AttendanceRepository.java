package in.westerncoal.biometric.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import in.westerncoal.biometric.model.Attendance;
import in.westerncoal.biometric.model.AttendanceKey;
import in.westerncoal.biometric.model.Terminal;

 
public interface AttendanceRepository extends JpaRepository<Attendance, AttendanceKey> {
	@EntityGraph(attributePaths = "serverPullLog")
	List<Attendance> findAttendanceWithServerPullLogBy();
}
 