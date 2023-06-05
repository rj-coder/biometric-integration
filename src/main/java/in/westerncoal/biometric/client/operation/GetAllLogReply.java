package in.westerncoal.biometric.client.operation;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import in.westerncoal.biometric.app.DeviceOperation;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class GetAllLogReply implements DeviceOperation {
	public String ret;
	public String sn;
	public boolean result;
	public int count;
	@JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd",timezone = "Asia/Calcutta")
	public Date from;
	@JsonFormat(shape = Shape.STRING,pattern = "yyyy-MM-dd",timezone = "Asia/Calcutta")
	public Date to;
	public List<Record> record;
	
	public boolean isEmptyReply() {
		return this.count == 0 || record.size() == 0 ;
	}
}

