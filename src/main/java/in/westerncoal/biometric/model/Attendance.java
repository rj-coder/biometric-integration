package in.westerncoal.biometric.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Value;

@Entity
@Table(name = "attendance",schema="bio")
@Value
@Builder
public class Attendance {
	@EmbeddedId
	private AttendanceKey AttendanceKey;
	@Column
	@Builder.Default
	private boolean uploadFlag = false;

	@OneToOne(optional = true)
	private ServerPullLog serverLog;
	
	@OneToOne(optional = true)
	private TerminalSendLog terminalLog;

	@OneToOne(optional = false)	
	private Terminal terminal;

}
