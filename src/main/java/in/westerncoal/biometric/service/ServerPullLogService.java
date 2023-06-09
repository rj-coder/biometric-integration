package in.westerncoal.biometric.service;

import org.springframework.stereotype.Component;

import in.westerncoal.biometric.model.ServerPullLog;

@Component
public interface ServerPullLogService {
	  ServerPullLog saveServerPullLog(ServerPullLog serverPullLog);
}
