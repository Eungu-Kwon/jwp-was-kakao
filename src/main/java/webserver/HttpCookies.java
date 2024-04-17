package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpCookies {
	private final Map<String, HttpCookie> cookies;

	public HttpCookies() {
		this.cookies = new HashMap<>();
	}

	public HttpCookies(String cookie) {
		cookies = new HashMap<>();
		if (cookie == null) {
			return;
		}
		String[] cookieArray = cookie.split(";");
		for (String c : cookieArray) {
			addCookie(c);
		}
	}

	public void addCookie(String cookieLine) {
		String[] cookieArray = cookieLine.trim().split("=");
		if (cookieArray.length == 2) {
			HttpCookie cookie = new HttpCookie(cookieArray[1]);
			cookie.setAttributes("Path", "/");
			cookies.put(cookieArray[0], cookie);
		}
	}

	public Map<String, HttpCookie> getCookies() {
		return cookies;
	}
}
