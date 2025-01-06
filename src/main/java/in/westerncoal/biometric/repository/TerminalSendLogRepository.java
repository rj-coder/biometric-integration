package in.westerncoal.biometric.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import in.westerncoal.biometric.model.TerminalSend;

public interface TerminalSendLogRepository extends JpaRepository<TerminalSend, String> {

}
