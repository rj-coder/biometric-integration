package in.westerncoal.biometric.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationLog;

public interface TerminalOperationLogRepository extends JpaRepository<TerminalOperationLog, String>{

	//public TerminalOperationLog findFirstByTerminalOrderByLastOperationTimeDesc(Terminal terminal);

 }
