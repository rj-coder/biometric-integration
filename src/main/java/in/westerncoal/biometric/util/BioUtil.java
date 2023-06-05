package in.westerncoal.biometric.util;

import java.text.SimpleDateFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Value;

@Value
public class BioUtil {

	private static ObjectMapper objectMapper;
	private static SimpleDateFormat dateTimeFormatter;
	private static SimpleDateFormat dateFormatter;
	static {
		objectMapper = new ObjectMapper();
		dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	}

	public static ObjectMapper getObjectMapper() {
		return objectMapper;
	}

	public static SimpleDateFormat getDateTimeFormatter() {
		return dateTimeFormatter;
	}

	public static SimpleDateFormat getDateFormatter() {
		return dateFormatter;
	}

}
