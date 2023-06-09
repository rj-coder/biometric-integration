package in.westerncoal.biometric.job;

import java.util.HashMap;
import java.util.Optional;

import org.java_websocket.WebSocket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import in.westerncoal.biometric.app.Device;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("singleton")
public class BiometricDevicePool extends HashMap<String, Device> {

	private static final long serialVersionUID = 8098045722662496174L;

	public Device getDevice(String serialNo) {
		return get(serialNo);
	}

	public Optional<Device> removeDevice(WebSocket conn) {
		for (String sn : this.keySet()) {
			if (this.get(sn).getWebSocket() == conn) {
				return Optional.ofNullable(this.remove(sn));
			} else
				continue;
		}
		return Optional.empty();
	}

	public String getDeviceSn(WebSocket conn) {
		for (String sn : this.keySet()) {
			if (this.get(sn).getWebSocket() == conn) {
				return sn;
			} else
				continue;
		}
		return null;
	}

	public void removeDevice(String sn) {
		this.remove(sn);
	}

}