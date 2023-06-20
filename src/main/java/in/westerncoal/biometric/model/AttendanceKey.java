package in.westerncoal.biometric.model;

import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AttendanceKey {
	@Column(length = 8)
	private Integer personnelNumber;
	private Timestamp punchDateTime;
}
