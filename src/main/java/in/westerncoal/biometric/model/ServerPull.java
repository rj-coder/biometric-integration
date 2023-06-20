package in.westerncoal.biometric.model;

import java.sql.Timestamp;
import java.util.Objects;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;
import in.westerncoal.biometric.enums.PullStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "server_pull", schema = "bio")
@Builder
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ServerPull {
	@Override
	public int hashCode() {
		return Objects.hash(pullId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerPull other = (ServerPull) obj;
		return pullId.compareTo(other.pullId) == 0;
	}

	@Id
	@UuidGenerator(style = Style.RANDOM)
	private String pullId;

	private String serverId;

	@CreationTimestamp
	private Timestamp pullStartTime;

	private String pullCommand;

	private Character pullType;

	@Builder.Default
	private PullStatus pullStatus = PullStatus.IN_PROGRESS;

}
