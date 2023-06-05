package in.westerncoal.biometric.app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import in.westerncoal.biometric.types.MessageType;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Message {
	static ObjectMapper objectMapper = new ObjectMapper();

	public static MessageType getMessageType(String message) {
		try {
			JsonNode jsonNode = objectMapper.readTree(message);

			if (jsonNode.has("cmd"))
				if (jsonNode.get("cmd").asText().compareTo("reg") == 0)
					return MessageType.DEVICE_INIT_REGISTER_MSG;
				else if (jsonNode.get("cmd").asText().compareTo("sendlog") == 0)
					return MessageType.DEVICE_SENDLOG_MSG;
				else if (jsonNode.get("cmd").asText().compareTo("senduser") == 0)
					return MessageType.DEVICE_SENDUSER_MSG;
				else {
					log.error("Unknown Error Message {}", message);
					return MessageType.UNKNOWN_MSG;
				}
			else if (jsonNode.has("ret")) {
				if (jsonNode.get("ret").asText().compareTo("getalllog") == 0)
					return MessageType.DEVICE_GETALLLOG_REPLY_MSG;
				else
					log.error("Unknown Error Message {}", message);
				return MessageType.UNKNOWN_MSG;
			} else {
				log.error("Unknown Error Message {}", message);
				return MessageType.UNKNOWN_MSG;
			}
		} catch (JsonMappingException e) {
			return MessageType.UNKNOWN_MSG;
		} catch (JsonProcessingException e) {
			log.error("Invalid JSON Data {}", message);
			return MessageType.UNKNOWN_MSG;
		}

	}
}