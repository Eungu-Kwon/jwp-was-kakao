package httpmessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import webserver.HttpCookie;

public class Header {
	private final Map<String, String> headers;
	private HttpCookie cookie;

	public Header(Map<String, String> headers) {
		if (headers == null) {
			this.headers = new HashMap<>();
			return;
		}
		this.headers = new HashMap<>(headers);
		cookie = null;
	}

	public void setCookie(HttpCookie cookie) {
		this.cookie = cookie;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		headers.forEach((key, value) -> {
			sb.append(key).append(": ").append(value).append("\r\n");
		});
		if (cookie != null) {
			cookie.getCookies().forEach((key, value) -> {
				sb.append("Set-Cookie: ").append(key).append("=").append(value).append("; Path=/\r\n");
			});
		}
		return sb.toString();
	}

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
}
