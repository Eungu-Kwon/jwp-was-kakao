package httpmessage;

import java.util.HashMap;
import java.util.Map;

import webserver.HttpRequest;
import webserver.constants.HttpMethods;

public class RequestLine {
	private static final int METHOD_INDEX = 0;
	private static final int PATH_INDEX = 1;
	private final HttpMethods method;
	private final String path;
	private final String version;
	private final Map<String, String> queryParam;

	public RequestLine(String line) {
		String[] tokens = line.split(" ");
		validateRequestLine(tokens);
		this.method = HttpMethods.valueOf(tokens[METHOD_INDEX]);
		this.path = tokens[PATH_INDEX];
		String[] pathToken = this.path.split("\\?");
		this.queryParam = pathToken.length > 1 ? parseQueryString(pathToken[1]) : new HashMap<>();
		this.version = tokens[2];
	}

	public HttpMethods getMethod() {
		return method;
	}

	public String getPath() {
		return path;
	}

	public Map<String, String> getQueryParam() {
		return queryParam;
	}

	private Map<String, String> parseQueryString(String query) {
		return HttpRequest.parseKeyValuePairs(query);
	}

	private static void validateRequestLine(String[] tokens) {
		if (tokens == null || tokens.length < 2) {
			throw new IllegalArgumentException("Invalid request line");
		}
	}
}
