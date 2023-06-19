package in.westerncoal.biometric.server.operation;

import java.util.Calendar;

import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

import in.westerncoal.biometric.enums.TerminalStatus;
import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalOperationLog;
import in.westerncoal.biometric.model.TerminalOperationStatus;
import in.westerncoal.biometric.service.TerminalService;
import in.westerncoal.biometric.types.MessageType;
import in.westerncoal.biometric.util.BioUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

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
