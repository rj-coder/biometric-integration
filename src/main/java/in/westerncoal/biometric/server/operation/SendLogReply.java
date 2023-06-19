package in.westerncoal.biometric.server.operation;

import java.util.Calendar;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.westerncoal.biometric.enums.MessageType;
import in.westerncoal.biometric.util.BioUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Builder
public class SendLogReply{
	@Builder.Default
	public String ret = "sendlog";
	@Builder.Default
	public boolean result = true;
	public Long count;
	public Long logindex;
	@Builder.Default
	public String cloudtime=BioUtil.getDateTimeFormatter().format(Calendar.getInstance().getTime());
	@Builder.Default
 	public int access = 1;

	@JsonIgnore
	public MessageType getMessageType() {
		return MessageType.DEVICE_SENDLOG_REPLY_MSG;
	}
	 
}
