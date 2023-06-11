package in.westerncoal.biometric.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.westerncoal.biometric.model.ServerPull;

public interface ServerPullLogRepository extends JpaRepository<ServerPull, Long> {

}
