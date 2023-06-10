package in.westerncoal.biometric.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendance", schema = "bio")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Attendance {
	@EmbeddedId
	private AttendanceKey AttendanceKey;

	@Builder.Default
	private boolean uploadFlag = false;

 	@ManyToOne
 	@JoinColumn(name="pull_id")
  	private ServerPullLog serverPullLog;
 	
 	@ManyToOne(cascade = CascadeType.ALL)
 	@JoinColumn(name="terminal_id")
  	private Terminal terminal;
  
	@OneToOne( cascade=CascadeType.ALL)
	@JoinColumn(name="send_id")
	private TerminalSendLog terminalSendLog;

}
