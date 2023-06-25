package in.westerncoal.biometric.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Calcutta")
 	private Timestamp punchDateTime;
}
