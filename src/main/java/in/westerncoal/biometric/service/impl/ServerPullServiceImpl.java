package in.westerncoal.biometric.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.westerncoal.biometric.model.ServerPull;
import in.westerncoal.biometric.repository.ServerPullRepository;
import in.westerncoal.biometric.service.ServerPullService;
import jakarta.transaction.Transactional;

@Service
public class ServerPullServiceImpl implements ServerPullService {

	@Autowired
	ServerPullRepository serverPullRepository;

	@Transactional
	public ServerPull saveServerPull(ServerPull serverPull) {
		return serverPullRepository.save(serverPull);
	}
 

	@Override
	public ServerPull findBy(String pullId) {
		
		return serverPullRepository.findById(pullId).orElse(null);
	}

 

}
