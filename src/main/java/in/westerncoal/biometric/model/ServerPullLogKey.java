package in.westerncoal.biometric.model;

import java.util.UUID;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class ServerPullLogKey {

	private String pullId;
	
	private String terminalId;
}
