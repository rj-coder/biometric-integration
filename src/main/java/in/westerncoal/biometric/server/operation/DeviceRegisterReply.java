package in.westerncoal.biometric.server.operation;

import in.westerncoal.biometric.app.DeviceOperation;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=false)
public class DeviceRegisterReply implements DeviceOperation {
	private String ret = "reg";
	private boolean result = true;
	private String cloudtime;
	private boolean nosenduser = true;

	public DeviceRegisterReply(String cloudtime) {
		super();
		this.cloudtime = cloudtime;
	}

}
