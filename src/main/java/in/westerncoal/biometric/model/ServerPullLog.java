package in.westerncoal.biometric.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "server_pull_log")
public class ServerPullLog {

	@EmbeddedId
	ServerPullLogKey serverPullLogKey;
	
	PullStatus pullStatus;
	
	
	@MapsId("pullId")
	@ManyToOne
	@JoinColumn(name = "pull_id")
	ServerPull serverPull;

	@MapsId("terminalId")
	@ManyToOne
	@JoinColumn(name = "terminal_id")
	Terminal terminal;
	
	

}
