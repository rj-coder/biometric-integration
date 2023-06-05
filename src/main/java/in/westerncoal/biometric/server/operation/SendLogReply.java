package in.westerncoal.biometric.server.operation;

import in.westerncoal.biometric.app.DeviceOperation;

public class SendLogReply implements DeviceOperation {
	public String ret = "sendlog";
	public boolean result = true;
	public int count;
	public int logindex;
	public String cloudtime;
 	public int access = 1;
 	
	public SendLogReply(boolean result, int count, int logindex, String cloudtime, int access) {
		super();	
		this.result = result;
		this.count = count;
		this.logindex = logindex;
		this.cloudtime = cloudtime;
		this.access = access;
	}
}
