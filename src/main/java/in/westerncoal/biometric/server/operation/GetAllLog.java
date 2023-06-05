package in.westerncoal.biometric.server.operation;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion;

import in.westerncoal.biometric.app.DeviceOperation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class GetAllLog implements DeviceOperation {
	public String cmd = "getalllog";
	public  boolean stn = true;
	@JsonFormat(shape=Shape.STRING,pattern="yyyy-MM-dd",timezone="Asia/Calcutta")
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(Include.NON_NULL)
	public Date from;
	@JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Calcutta")
	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(Include.NON_NULL)
	public Date to;

	public GetAllLog(Date from, Date to) {
		super();
		this.from = from;
		this.to = to;
	}

}
