package httpmessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Header {
	private final Map<String, String> headers;

	public Header(Map<String, String> headers) {
		if (headers == null) {
			this.headers = new HashMap<>();
			return;
		}
		this.headers = new HashMap<>(headers);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		headers.forEach((key, value) -> {
			sb.append(key).append(": ").append(value).append("\r\n");
		});
		return sb.toString();
	}

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
}
