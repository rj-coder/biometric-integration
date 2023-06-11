package in.westerncoal.biometric.job;

import java.util.HashMap;
import java.util.Optional;
import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import in.westerncoal.biometric.app.Device;
import in.westerncoal.biometric.service.TerminalService;
import in.westerncoal.biometric.types.DeviceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@Scope("singleton")
public class BiometricDevicePool extends HashMap<String, Device> {
	@Autowired
	private TerminalService terminalService;

	private static final long serialVersionUID = 8098045722662496174L;

	public Device getDevice(String serialNo) {
		return get(serialNo);
	}

	public Optional<Device> setDeviceInactive(WebSocket conn) {
		for (String sn : this.keySet()) {
			if (this.get(sn).getWebSocket() == conn) {
				this.get(sn).setDeviceStatus(DeviceStatus.DEVICE_INACTIVE);
				terminalService.setTerminalInactive(sn);
				return Optional.ofNullable(this.getDevice(sn));
			}
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