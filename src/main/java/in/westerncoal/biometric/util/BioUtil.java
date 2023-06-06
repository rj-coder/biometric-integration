package in.westerncoal.biometric.util;

import java.text.SimpleDateFormat;

import com.fasterxml.jackson.annotation.JsonFormat.Feature;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import lombok.Value;

@Value
public class BioUtil {

	private static ObjectMapper objectMapper;
	private static SimpleDateFormat dateTimeFormatter;
	private static SimpleDateFormat dateFormatter;
	static {
		JsonFactory f = JsonFactory.builder().enable(JsonReadFeature.ALLOW_TRAILING_COMMA).build();
		objectMapper = JsonMapper.builder(f).build();

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
