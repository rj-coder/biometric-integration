package in.westerncoal.biometric.client.operation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class Record {
	private Integer enrollid;
	@JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Calcutta")
	private Date time;
	private Integer mode;
	private Integer inout;
	private Integer event;
	private Integer temp;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private Integer verifymode;
	@JsonIgnoreProperties(ignoreUnknown = true)
	private String image;
}
