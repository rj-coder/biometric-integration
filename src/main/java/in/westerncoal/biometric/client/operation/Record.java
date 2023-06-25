package in.westerncoal.biometric.client.operation;

 
import java.sql.Timestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class Record {
	private Integer enrollid;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  	private Timestamp time;
	private Integer mode;
	private Integer inout;
	private Integer event;
	private Integer temp;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Integer verifymode;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String image;
}
