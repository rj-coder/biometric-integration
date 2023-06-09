package in.westerncoal.biometric.model;

import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Value;

@Embeddable
@Value
public class AttendanceKey {
	@Column(length = 8)
	private Integer personnelNumber;
	private Date punchDate;
	private Time punchTime;

}
