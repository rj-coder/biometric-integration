package in.westerncoal.biometric.model;

import java.sql.Timestamp;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.Value;

@Entity
@Table(name = "server_pull", schema = "bio")
@Value
@Builder
@AllArgsConstructor
@Data
public class ServerPull {
	@Id
	@UuidGenerator(style = Style.RANDOM)
	private String pullId;
	private String serverId;
	@CreationTimestamp
	private Timestamp pullTime;
	private String pullCommand;
	private Character pullType;
 
// 	@ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
//	@JoinTable(name = "server_log", inverseJoinColumns = @JoinColumn(name = "terminal_id", referencedColumnName = "terminal_id"), joinColumns = @JoinColumn(name = "pull_id", referencedColumnName = "pullId"), uniqueConstraints = @UniqueConstraint(columnNames = {
//			"TERMINAL_ID", "PULL_ID" }))
// 	public Set<Terminal> terminals;

 	 
}
