package in.westerncoal.biometric.model;

import java.sql.Timestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "terminal_send",schema="fbm2")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TerminalSend {
	@Id
	@UuidGenerator(style = Style.RANDOM)
	@Column(name = "send_id")
	private String sendId;
	
	@CreationTimestamp
	private Timestamp sendTime;
	private String sendCommand;
	
	private String terminalId;
	
 }
