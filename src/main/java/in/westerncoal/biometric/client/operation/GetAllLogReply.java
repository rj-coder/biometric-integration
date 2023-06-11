package in.westerncoal.biometric.client.operation;

import java.util.List;
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
 	public Long from;
 	public Long to;
	 
 	public List<Record> record;
 	public boolean isEmptyReply() {
		return this.count == 0 || record.size() == 0;
	}
 	
  
}
