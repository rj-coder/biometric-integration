package in.westerncoal.biometric.model;

import java.sql.Timestamp;
import java.util.List;
import org.hibernate.annotations.UpdateTimestamp;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "terminal", schema = "bio")
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Terminal {
	@Id
	private String bioTerminalSn;

	@Builder.Default
	private BioTerminalStatus bioTerminalStatus = BioTerminalStatus.ACTIVE;

	@UpdateTimestamp
	private Timestamp lastTimestamp;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="server_log",joinColumns = @JoinColumn(referencedColumnName = "bioTerminalSn"), inverseJoinColumns = @JoinColumn(referencedColumnName = "pullId")
	// , uniqueConstraints = @UniqueConstraint(columnNames = {
	// "server_pull_log_list_pull_id", "bio_terminal_list_bio_terminal_sn" })
	)
	@ToString.Exclude
	public List<ServerPullLog> serverLog;

	@OneToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "terminal_log", joinColumns = @JoinColumn(referencedColumnName = "bioTerminalSn"), inverseJoinColumns = @JoinColumn(referencedColumnName = "sendId"))
	@ToString.Exclude
	public List<TerminalSendLog> terminalLog;
}
