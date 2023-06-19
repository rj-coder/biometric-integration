package in.westerncoal.biometric.client.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.westerncoal.biometric.types.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
@AllArgsConstructor
public class TerminalRegister {
	private String cmd;
	private String sn;
	private Devinfo devinfo;
	
	@JsonIgnore
	public MessageType getMessageType() {
		return MessageType.DEVICE_INIT_REGISTER_MSG;
	}

}
