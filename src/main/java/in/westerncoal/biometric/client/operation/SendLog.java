package in.westerncoal.biometric.client.operation;

import java.util.List;

import in.westerncoal.biometric.app.DeviceOperation;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Value
@EqualsAndHashCode(callSuper = false)
public class SendLog implements DeviceOperation{
	private String cmd;
	private String sn;
	private int count;
	private int logindex;
	private List<Record> record;
	 
}
