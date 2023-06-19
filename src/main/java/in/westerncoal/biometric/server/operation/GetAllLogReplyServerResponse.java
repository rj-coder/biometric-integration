package in.westerncoal.biometric.server.operation;

import com.fasterxml.jackson.annotation.JsonIgnore;

import in.westerncoal.biometric.enums.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllLogReplyServerResponse {
	@Builder.Default
	public String cmd="getalllog";
	@Builder.Default
	public boolean stn=false;
	
	@JsonIgnore
	public MessageType getMessageType() {
		return MessageType.DEVICE_GETALLLOG_REPLY_RESPONSE_MSG;
	}

}
