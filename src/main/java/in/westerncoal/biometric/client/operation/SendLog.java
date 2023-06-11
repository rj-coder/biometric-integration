package in.westerncoal.biometric.client.operation;

import java.util.ArrayList;
import java.util.List;

import in.westerncoal.biometric.app.DeviceOperation;
import in.westerncoal.biometric.model.AttendanceKey;
import in.westerncoal.biometric.model.Terminal;
import in.westerncoal.biometric.model.TerminalSend;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import in.westerncoal.biometric.model.Attendance;

@Jacksonized
@Builder
@Value
public class SendLog implements DeviceOperation {
	private String cmd;
	private String sn;
	private int count;
	private int logindex;
	private List<Record> record;

	public List<Attendance> getAttendanceList(Terminal terminal, TerminalSend terminalSendLog) {
		List<Attendance> attendanceList = new ArrayList<Attendance>();
		for (Record rec : record) {
			AttendanceKey attendanceKey = new AttendanceKey(rec.getEnrollid(),
					new java.sql.Date(rec.getTime().getTime()), new java.sql.Time(rec.getTime().getTime()));

			Attendance attendance = Attendance.builder().AttendanceKey(attendanceKey).terminalSendLog(terminalSendLog).terminal(terminal)
					.build();
			attendanceList.add(attendance);
		}
		return attendanceList;
	}

}
