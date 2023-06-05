package in.westerncoal.biometric.client.operation;

import in.westerncoal.biometric.app.DeviceOperation;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized 
@Builder
public class DeviceRegister implements DeviceOperation {
	private String cmd;
	private String sn;
	private Devinfo devinfo; 

}
