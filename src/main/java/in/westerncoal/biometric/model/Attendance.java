package in.westerncoal.biometric.model;

import java.sql.Date;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Immutable;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attendance",schema="fbm2")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Immutable
public class Attendance{
	@EmbeddedId
	private AttendanceKey AttendanceKey;

	@Builder.Default
	private boolean uploadFlag = false;

	private String pullId;

	private String sendId;

	private String terminalId;

	@CreationTimestamp
	private LocalDateTime createTimestamp;
}
