package in.westerncoal.biometric.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import in.westerncoal.biometric.model.Terminal;

public interface TerminalRepository extends JpaRepository<Terminal, String> {
	@Modifying
	@Query("UPDATE Terminal t " +
	       "SET t.terminalStatus = :#{#terminal.terminalStatus}, " +
	       "t.lastAccessTimestamp = :#{#terminal.lastAccessTimestamp}, " +
	       "t.terminalAddress = :#{#terminal.terminalAddress} " +
	       "WHERE t.terminalId = :#{#terminal.terminalId}")
	void updateTerminal(@Param("terminal") Terminal terminal);

}
