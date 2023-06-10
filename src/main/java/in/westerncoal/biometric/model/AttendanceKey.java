package in.westerncoal.biometric.model;

import java.sql.Date;
import java.sql.Time;
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
	private Date punchDate;
	private Time punchTime;
}
