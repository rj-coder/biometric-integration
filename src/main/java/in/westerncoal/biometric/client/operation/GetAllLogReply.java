package in.westerncoal.biometric.client.operation;

import java.util.ArrayList;
import java.util.List;
import in.westerncoal.biometric.model.Attendance;
import in.westerncoal.biometric.model.AttendanceKey;
import in.westerncoal.biometric.model.ServerPullLog;
import in.westerncoal.biometric.types.MessageType;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
public class GetAllLogReply {
	public String ret;
	public String sn;
	public boolean result;
	public int count;
	public Long from;
	public Long to;

	public List<Record> record;

	public boolean isEmptyReply() {
		return this.count == 0 || record.size() == 0;
	}

	public Object getMessageType() {
		return MessageType.DEVICE_GETALLLOG_REPLY_MSG;
	}

	public List<Attendance> getAttendanceList(ServerPullLog serverPullLog) {
		List<Attendance> attendanceList = new ArrayList<Attendance>();
		for (Record rec : record) {
			AttendanceKey attendanceKey = new AttendanceKey(rec.getEnrollid(),
					new java.sql.Timestamp(rec.getTime().getTime()));

			Attendance attendance = Attendance.builder().AttendanceKey(attendanceKey)
					.terminalId(serverPullLog.getServerPullLogKey().getTerminalId())
					.pullId(serverPullLog.getServerPullLogKey().getPullId()).build();
			attendanceList.add(attendance);
		}
		return attendanceList;
	}
}
