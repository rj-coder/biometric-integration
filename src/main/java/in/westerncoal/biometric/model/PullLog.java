package in.westerncoal.biometric.model;

import java.sql.Timestamp;

import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name="pull_log")
public class PullLog {
	@Id
	@GeneratedValue(generator = "pull_log_id", strategy = GenerationType.AUTO)
	@SequenceGenerator( name = "pull_log_id")
	private Long id;
	private Timestamp pull_datetime;
	 
	
	
	

}
