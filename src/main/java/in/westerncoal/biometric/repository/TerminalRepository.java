package in.westerncoal.biometric.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationLog;

public interface TerminalRepository extends JpaRepository<Terminal, String> {
//	@EntityGraph(attributePaths = "pullLogList")
//	List<Terminal> findBioTerminalsWithPullLogBy();
	@Query("update Terminal t set t.terminalStatus = 0 where t.terminalId = :terminalId")
	@Modifying
	@Transactional

	public void update(String terminalId);


}
