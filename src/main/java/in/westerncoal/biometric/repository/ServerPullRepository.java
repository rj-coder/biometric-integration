package in.westerncoal.biometric.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import in.westerncoal.biometric.model.ServerPull;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.ServerPullLogKey;

public interface ServerPullRepository extends JpaRepository<ServerPull, String> {

}
