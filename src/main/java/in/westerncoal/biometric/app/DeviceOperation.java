package in.westerncoal.biometric.app;

import org.java_websocket.WebSocket;
import in.westerncoal.biometric.job.BiometricDevicePool;
import in.westerncoal.biometric.types.DeviceStatus;

public interface DeviceOperation {

	public default Device doDeviceAction(String sn, WebSocket ws, BiometricDevicePool biometricDevicePool,
			DeviceOperation deviceOperation, boolean isCompleted) {
		Device device = new Device(sn, ws, DeviceStatus.DEVICE_ACTIVE, deviceOperation, isCompleted);
		biometricDevicePool.put(sn, device);
		return device;
	}
}
