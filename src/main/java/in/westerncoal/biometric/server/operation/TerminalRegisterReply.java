package in.westerncoal.biometric.server.operation;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.westerncoal.biometric.enums.MessageType;
import in.westerncoal.biometric.util.BioUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Builder
@AllArgsConstructor
@Value
public class TerminalRegisterReply {
	private String ret = "reg";
	private boolean result = true;
	private final String cloudtime = BioUtil.getDateTimeFormatter().format(Calendar.getInstance().getTime());

	private boolean nosenduser = true;

	@JsonIgnore
	public MessageType getMessageType() {
		return MessageType.DEVICE_INIT_REGISTER_REPLY_MSG;
	}
}
