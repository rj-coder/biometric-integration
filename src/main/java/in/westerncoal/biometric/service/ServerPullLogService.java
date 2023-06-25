package in.westerncoal.biometric.service;

import org.springframework.stereotype.Component;

import in.westerncoal.biometric.model.ServerPull;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.model.ServerPull;

@Component
public interface ServerPullLogService {
	
	ServerPullLog saveNew(ServerPullLog serverPullLog);

 }
