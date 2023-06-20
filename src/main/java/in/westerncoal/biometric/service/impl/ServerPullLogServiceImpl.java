package in.westerncoal.biometric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.repository.ServerPullLogRepository;
import in.westerncoal.biometric.service.ServerPullLogService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ServerPullLogServiceImpl implements ServerPullLogService {

	@Autowired
	ServerPullLogRepository serverPullLogRepository;
	
	@PersistenceContext
	EntityManager em;

	@Transactional
 	public ServerPullLog saveServerPullLog(ServerPullLog serverPullLog) {
 		em.persist(serverPullLog);
		return serverPullLog;
	}

}
