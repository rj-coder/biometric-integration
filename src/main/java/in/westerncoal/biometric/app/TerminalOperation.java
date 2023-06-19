package in.westerncoal.biometric.app;

import in.westerncoal.biometric.model.Terminal;
import lombok.Builder;
import lombok.Value;
import in.westerncoal.biometric.types.MessageType;

@Builder
@Value
public class TerminalOperation {

	private Terminal terminal;

	private MessageType MessageType;

	private OperationStatus operationStatus;
	
	

 
}
