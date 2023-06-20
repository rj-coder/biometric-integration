package in.westerncoal.biometric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.westerncoal.biometric.model.ServerPull;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.repository.ServerPullLogRepository;
import in.westerncoal.biometric.repository.ServerPullRepository;
import in.westerncoal.biometric.service.ServerPullLogService;
import in.westerncoal.biometric.service.ServerPullService;
import jakarta.transaction.Transactional;

@Service
public class ServerPullLogServiceImpl implements ServerPullLogService {

	@Autowired
	ServerPullLogRepository serverPullLogRepository;

	@Transactional
	public ServerPullLog saveServerPullLog(ServerPullLog serverPullLog) {
		return serverPullLogRepository.save(serverPullLog);
	}

}
