package in.westerncoal.biometric.model;

import in.westerncoal.biometric.enums.PullStatus;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "server_pull_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ServerPullLog {

	@EmbeddedId
	private ServerPullLogKey serverPullLogKey;

	private PullStatus pullStatus;

	@MapsId("pullId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pull_id", unique = false,nullable = false,insertable = false,updatable = false)
	public ServerPull serverPull;

	@MapsId("terminalId")
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "terminal_id", unique = false,nullable=false,insertable = false,updatable = false)
	public Terminal terminal;

}
