package in.westerncoal.biometric.service;

import org.springframework.stereotype.Component;

import in.westerncoal.biometric.model.ServerPull;

@Component
public interface ServerPullLogService {
	  ServerPull saveServerPullLog(ServerPull serverPullLog);
}
