package httpmessage;

import java.io.IOException;
import java.util.Map;

public class Header {
	private final Map<String, String> headers;

	public Header(Map<String, String> headers) {
		this.headers = headers;
	}

	public String toString() {
		if (headers == null) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		headers.forEach((key, value) -> {
			sb.append(key).append(": ").append(value).append("\r\n");
		});
		return sb.toString();
	}
}
