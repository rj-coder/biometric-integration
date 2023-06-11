package in.westerncoal.biometric.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.IdClass;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Embeddable
@Data
@NoArgsConstructor
public class ServerPullLogKey {

	private String pullId;
	private String terminalId;
}
