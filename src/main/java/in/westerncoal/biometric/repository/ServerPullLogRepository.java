package in.westerncoal.biometric.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import in.westerncoal.biometric.model.ServerPullLog;

public interface ServerPullLogRepository extends JpaRepository<ServerPullLog, Long> {

}
