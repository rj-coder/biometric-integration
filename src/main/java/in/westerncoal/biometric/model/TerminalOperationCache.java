package in.westerncoal.biometric.model;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.java_websocket.WebSocket;
import in.westerncoal.biometric.enums.TerminalStatus;

public class TerminalOperationCache {
	private static final Map<String, TerminalOperationLog> terminalMap = new ConcurrentHashMap<String, TerminalOperationLog>();

	public static void addTerminalOperation(TerminalOperationLog terminalOperationLog, WebSocket websocket) {
		terminalOperationLog.getTerminal().setWebSocket(websocket);
		terminalMap.put(terminalOperationLog.getTerminal().getTerminalId(), terminalOperationLog);
	}

	public static void removeTerminal(String terminalId) {
		terminalMap.remove(terminalId);
	}

	public static TerminalOperationLog getTerminalOperationLog(String terminalId) {
		return terminalMap.get(terminalId);
	}

	public static Terminal getTerminal(String terminalId) {
		TerminalOperationLog terminalOperationLog = terminalMap.get(terminalId);
		if (terminalOperationLog != null) {
			return terminalOperationLog.getTerminal();
		}
		return null;
	}

	public static void updateTerminalOperation(TerminalOperationLog updatedLog, WebSocket websocket) {
		updatedLog.getTerminal().setWebSocket(websocket);
		terminalMap.put(updatedLog.getTerminal().getTerminalId(), updatedLog);
	}

	public static void updateTerminalOperation(TerminalOperationLog updatedLog) {
		terminalMap.put(updatedLog.getTerminal().getTerminalId(), updatedLog);
	}

	public static void updateTerminalStatus(String terminalId, TerminalStatus newStatus) {
		TerminalOperationLog terminalOperationLog = terminalMap.get(terminalId);
		if (terminalOperationLog != null) {
			Terminal terminal = terminalOperationLog.getTerminal();
			terminal.setTerminalStatus(newStatus);
			terminalOperationLog.setTerminal(terminal);
		}
	}

	public static List<Terminal> getActiveTerminals() {
		return terminalMap.values().stream().filter(
				terminalOperationLog -> (terminalOperationLog.getTerminal().getTerminalStatus() == TerminalStatus.ACTIVE
						&& terminalOperationLog.getTerminal().getWebSocket() != null
						&& !terminalOperationLog.getTerminal().getWebSocket().isClosed()))
				.map(TerminalOperationLog::getTerminal).collect(Collectors.toList());
	}

	public static Terminal getTerminalByWebSocket(WebSocket webSocket) {
		return terminalMap.values().stream()
				.filter(terminalOperationLog -> terminalOperationLog.getTerminal().getWebSocket() == webSocket)
				.map(TerminalOperationLog::getTerminal).findFirst().orElse(null);
	}

	public static TerminalOperationLog getTerminalOperationLog(Terminal terminal) {
		return terminalMap.get(terminal.getTerminalId());

	}

}
