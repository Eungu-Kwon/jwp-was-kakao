package webserver;

import java.util.HashMap;
import java.util.Map;

public class HttpCookie {
	String value;

	Map<String, String> attributes = new HashMap<>();

	public HttpCookie(String value) {
		this.value = value;
	}

	public void setAttributes(String key, String value) {
		attributes.put(key, value);
	}

	public String getValue() {
		return value;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}
}
