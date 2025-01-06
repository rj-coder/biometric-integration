package in.westerncoal.biometric.model;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Objects;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.java_websocket.WebSocket;
import in.westerncoal.biometric.enums.TerminalStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Table(name = "terminal",schema="fbm2")
@Builder 
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Terminal  {
	@Transient
    private final Lock lock = new ReentrantLock();

	
	@Id
	@Column(name = "terminal_id")
	private String terminalId;

	@Transient
	private WebSocket webSocket;

	@Builder.Default
	@Column(name = "terminal_status")
	private TerminalStatus terminalStatus = TerminalStatus.ACTIVE;

	@UpdateTimestamp
	private Timestamp lastAccessTimestamp;
	
	private String terminalAddress;
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Terminal other = (Terminal) obj;
		return terminalId.compareTo(other.terminalId) == 0;
	}

	@Override
	public int hashCode() {
		return Objects.hash(terminalId);
	}

	public boolean isActive() {

		return terminalStatus == TerminalStatus.ACTIVE;
	}

	public boolean isWebSocketClosed() {

		return webSocket.isClosed();
	}

}
