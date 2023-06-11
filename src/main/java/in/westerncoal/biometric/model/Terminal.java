package in.westerncoal.biometric.model;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "terminal", schema = "bio")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Builder
@NoArgsConstructor
public class Terminal {
	@Id
	@Column(name="terminal_id")
 	private String terminalId;

	@Builder.Default
	private TerminalStatus terminalStatus = TerminalStatus.ACTIVE;

	@UpdateTimestamp
	private Timestamp lastTimestamp;

//	@ManyToMany(fetch = FetchType.LAZY,mappedBy = "terminals")
// 	@ToString.Exclude 	
// 	public Set<ServerPullLog> serverLog;

//	
//	@OneToMany(fetch = FetchType.LAZY,mappedBy = "terminal")
// 	@ToString.Exclude 	
// 	public Set<TerminalSendLog> terminalLog;
}
