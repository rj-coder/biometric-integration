package in.westerncoal.biometric.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Entity
@Table(name = "attendance",schema="bio")
 @Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Attendance {
	@EmbeddedId
	private AttendanceKey AttendanceKey;
	@Column
	@Builder.Default
	private boolean uploadFlag = false;

	@OneToOne(optional = true,fetch = FetchType.LAZY)
	private ServerPullLog serverLog;
	
	@OneToOne(optional = true,fetch = FetchType.LAZY)
	private TerminalSendLog terminalLog;

	@OneToOne(optional = false,fetch = FetchType.LAZY)
	private Terminal terminal;

}
