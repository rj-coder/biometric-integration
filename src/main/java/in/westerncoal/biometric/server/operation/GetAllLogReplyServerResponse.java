package in.westerncoal.biometric.server.operation;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Value
@Builder
@NoArgsConstructor
public class GetAllLogReplyServerResponse {
	public String cmd="getalllog";
	public boolean stn=false;
}
