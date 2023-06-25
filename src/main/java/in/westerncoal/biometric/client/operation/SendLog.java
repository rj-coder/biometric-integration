package in.westerncoal.biometric.client.operation;

import java.util.ArrayList;
import java.util.List;

import in.westerncoal.biometric.model.AttendanceKey;
import in.westerncoal.biometric.model.TerminalSend;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;
import in.westerncoal.biometric.enums.MessageType;
import in.westerncoal.biometric.model.Attendance;

@Jacksonized
@Builder
@Value
public class SendLog  {
	private String cmd;
	private String sn;
	private Long count;
	private Long logindex;
	private List<Record> record;

	public List<Attendance> getAttendanceList( TerminalSend terminalSendLog) {
		List<Attendance> attendanceList = new ArrayList<Attendance>();
		for (Record rec : record) {
			AttendanceKey attendanceKey = new AttendanceKey(rec.getEnrollid(),
				 new java.sql.Date(rec.getTime().getTime()));

			Attendance attendance = Attendance.builder().AttendanceKey(attendanceKey)
					.terminalId(terminalSendLog.getTerminalId()).sendId(terminalSendLog.getSendId()).build();
			attendanceList.add(attendance);
		}
		return attendanceList;
	}
	
	public MessageType getMessageType() {
		return MessageType.DEVICE_SENDLOG_MSG;
	}

}
