package in.westerncoal.biometric.model;

 import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UuidGenerator.Style;

import in.westerncoal.biometric.enums.OperationType;
import in.westerncoal.biometric.enums.TerminalOperationStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "terminal_operation_log")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TerminalOperationLog {
	@Id
	@UuidGenerator(style = Style.RANDOM)
	private String terminalLogId;

	private String terminalId;

	private String pullId;

	private String sendId;

	@Enumerated(EnumType.STRING)
	private OperationType operationType;

  	@CreationTimestamp
	private Timestamp startOperationTime;
 	
  	@UpdateTimestamp
 	private Timestamp lastOperationTime;

	@Builder.Default
	private boolean recordFetchOperation = false;

	@Builder.Default
	private long recordCount = 0;

	@Builder.Default
	private long recordFetched = 0;

	@Enumerated(EnumType.STRING)
	private TerminalOperationStatus terminalOperationStatus;

	@Transient
	public Terminal terminal;

}
