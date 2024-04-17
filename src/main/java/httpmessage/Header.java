package httpmessage;

import java.util.HashMap;
import java.util.Map;

import webserver.HttpCookies;
import webserver.constants.HttpHeaders;

public class Header {
	private final Map<String, String> headers;
	private HttpCookies cookies;

	public Header(Map<String, String> headers) {
		if (headers == null) {
			this.headers = new HashMap<>();
			return;
		}
		this.headers = new HashMap<>(headers);
		cookies = new HttpCookies();
	}

	public void setCookie(String cookieLine) {
		cookies.addCookie(cookieLine);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		headers.forEach((key, value) -> {
			sb.append(key).append(": ").append(value).append("\r\n");
		});
		if (cookies != null) {
			cookies.getCookies().forEach((key, value) -> {
				sb.append(HttpHeaders.SET_COOKIE).append(": ").append(key).append("=").append(value.getValue());
				value.getAttributes().forEach((k, v) -> {
					sb.append("; ").append(k).append("=").append(v);
				});
				sb.append("\r\n");
			});

		}
		return sb.toString();
	}

	public void addHeader(String key, String value) {
		headers.put(key, value);
	}
}
