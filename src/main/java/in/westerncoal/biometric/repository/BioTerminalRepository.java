package in.westerncoal.biometric.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import in.westerncoal.biometric.model.Terminal;

public interface BioTerminalRepository extends JpaRepository<Terminal, String> {
	@EntityGraph(attributePaths = "pullLogList")
	List<Terminal> findBioTerminalsWithPullLogBy();
}
