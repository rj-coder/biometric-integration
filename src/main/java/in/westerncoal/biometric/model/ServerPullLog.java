package in.westerncoal.biometric.model;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Entity
@Table(name = "server_pull",schema="bio")
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
 public class ServerPullLog {
	@Id
	@UuidGenerator(style = Style.RANDOM)
	private String pullId;
	private String serverId;
	private Timestamp pullTime;
	private String pullCommand;
	private Character pullType;

	@ManyToMany(mappedBy = "serverLog")
	public List<Terminal> terminalList;

}
