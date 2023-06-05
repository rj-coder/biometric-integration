package in.westerncoal.biometric.client.operation;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Jacksonized
@Builder
public class Devinfo {
	private String modelname;
	private Integer usersize;
	private Integer facesize;
	private Integer palmsize;
	private Integer fpsize;
	private Integer cardsize;
	private Integer pwdsize;
	private Integer logsize;
	private Integer useduser;
	private Integer usedface;
	private Integer usedpalm;
	private Integer usedfp;
	private Integer usedcard;
	private Integer usedpwd;
	private Integer usedlog;
	private Integer usednewlog;
	private String fpalgo;
	private String firmware;
	private String time;
	private String mac;

		 
}