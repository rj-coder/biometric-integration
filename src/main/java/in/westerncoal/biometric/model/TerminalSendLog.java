package in.westerncoal.biometric.model;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "terminal_send", schema = "bio")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TerminalSendLog {
	@Id
	@UuidGenerator(style = Style.RANDOM)
	private String sendId;
	@CreationTimestamp
	private Timestamp sendTime;
	private String sendCommand;

	@ManyToOne
	@JoinColumn(name="terminal_id")
	public Terminal terminal;
}
