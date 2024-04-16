package webserver;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpCookie {
	private Map<String, String> cookies;

	public HttpCookie() {
		this.cookies = new HashMap<>();
	}

	public HttpCookie(String cookie) {
		cookies = new HashMap<>();
		if (cookie == null) {
			return;
		}
		String[] cookieArray = cookie.split(";");
		for (String c : cookieArray) {
			addCookie(c);
		}
	}

	void addCookie(String cookieLine) {
		String[] cookieArray = cookieLine.trim().split("=");
		if (cookieArray.length == 2) {
			cookies.put(cookieArray[0], cookieArray[1]);
		}
	}

	public Map<String, String> getCookies() {
		return cookies;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		cookies.forEach((key, value) -> {
			sb.append(key).append("=").append(value).append("; ");
		});
		sb.append("Path=/");
		return sb.toString();
	}
}
