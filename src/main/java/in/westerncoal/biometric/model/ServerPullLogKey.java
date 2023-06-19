package in.westerncoal.biometric.model;

import java.util.Objects;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Embeddable
@NoArgsConstructor
@Builder
@Getter
@Setter
@AllArgsConstructor
@ToString
 
public class ServerPullLogKey {

	private String pullId;

	private String terminalId;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerPullLogKey other = (ServerPullLogKey) obj;
		return pullId.compareTo(other.pullId) == 0 && terminalId.compareTo(other.terminalId) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pullId, terminalId);
	}
}
