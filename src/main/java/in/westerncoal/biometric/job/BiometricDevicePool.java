package in.westerncoal.biometric.job;

import java.beans.BeanProperty;
import java.util.HashMap;

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

	// private static final Map<String, Device> biometricDevicePool = new
	// HashMap<String,Device>();
	public Device getDevice(String serialNo) {
		return get(serialNo);
	}
	
	public void removeDevice(WebSocket conn) {
		for (String sn : this.keySet()) {
			if (this.get(sn).getWebSocket() != conn) {
				this.remove(sn);
			}
		}
	}

	public void removeDevice(String sn) {
		this.remove(sn);
	}

}