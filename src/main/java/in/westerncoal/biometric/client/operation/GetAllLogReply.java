package in.westerncoal.biometric.client.operation;

import java.util.Date;
import java.util.List;

import org.springframework.boot.jackson.JsonComponent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
