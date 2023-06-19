package in.westerncoal.biometric.model;

import java.util.concurrent.locks.ReentrantLock;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.concurrent.locks.Lock;

@Entity
@Table(name = "server_pull_log")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ServerPullLog {
	@Transient
	private final Lock lock = new ReentrantLock();
	
	@EmbeddedId
	private ServerPullLogKey serverPullLogKey;
	
}
