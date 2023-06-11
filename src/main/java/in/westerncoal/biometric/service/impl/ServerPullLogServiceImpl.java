package in.westerncoal.biometric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.westerncoal.biometric.model.ServerPull;
import in.westerncoal.biometric.repository.ServerPullLogRepository;
import in.westerncoal.biometric.service.ServerPullLogService;

@Service
public class ServerPullLogServiceImpl implements ServerPullLogService {

	@Autowired
	ServerPullLogRepository serverPullLogRepository;

	@Override
	public ServerPull saveServerPullLog(ServerPull serverPullLog) {
		return serverPullLogRepository.save(serverPullLog);
	}

}
