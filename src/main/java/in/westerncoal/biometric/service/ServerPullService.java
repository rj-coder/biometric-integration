package in.westerncoal.biometric.service;

import org.springframework.stereotype.Component;

import in.westerncoal.biometric.enums.PullStatus;
import in.westerncoal.biometric.model.ServerPull;

@Component
public interface ServerPullService {
	ServerPull saveServerPull(ServerPull serverPull);

 
	ServerPull findBy(String pullId);
 }
