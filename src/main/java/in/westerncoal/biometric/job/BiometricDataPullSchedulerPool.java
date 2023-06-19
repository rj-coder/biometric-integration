package in.westerncoal.biometric.job;

import java.util.HashMap;

import org.springframework.context.annotation.Scope;

import in.westerncoal.biometric.model.ServerPullLog;

@Scope("singleton")
public class BiometricDataPullSchedulerPool extends HashMap<String, ServerPullLog> {

	private static final long serialVersionUID = -641563399603655470L;

	public ServerPullLog getServerPull(String pullId) {
		return this.get(pullId);

	}

}
