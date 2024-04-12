package webserver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HttpRequest {

    private final String method;
    private final String path;
    private final Map<String, String> header;
    private final Map<String, String> queryParam;
    private final String body;
    private final Map<String, String> form;
    private static final int METHOD_INDEX = 0;
    private static final int PATH_INDEX = 1;

    public HttpRequest(String requestLine, Map<String, String> header, String body) {
        String[] tokens = requestLine.isEmpty() ? null : requestLine.split(" ");
        validateRequestLine(tokens);
        this.method = tokens[METHOD_INDEX];
        String[] pathToken = tokens[PATH_INDEX].split("\\?");
        this.path = pathToken[0];
        this.header = header;
        this.queryParam = pathToken.length > 1 ? parseQueryString(pathToken[1]) : new HashMap<>();
        this.body = body;
        this.form = parseForm();
    }

    public boolean isMethod(String method) {
        return this.method.equals(method);
    }

    public String getPath() {
        return path;
    }

    public Map<String, String> getForm() {
        return form;
    }

    public Map<String, String> getQuery() {
        return queryParam;
    }

    public String getMethod() {
        return method;
    }

    private Map<String, String> parseQueryString(String query) {
        return parseKeyValuePairs(query, "&");
    }

    private Map<String, String> parseForm() {
        return body == null ? new HashMap<>() : parseKeyValuePairs(body, "&");
    }

    private Map<String, String> parseKeyValuePairs(String input, String delimiter) {
        String[] tokens = input.split(delimiter);
        Map<String, String> map = new HashMap<>();
        for (String token : tokens) {
            List<String> keyValue = Arrays.stream(token.split("="))
                .map(String::trim)
                .collect(Collectors.toList());
            map.put(keyValue.get(0), keyValue.size() > 1 ? keyValue.get(1) : "");
        }
        return map;
    }

    private static void validateRequestLine(String[] tokens) {
        if (tokens == null || tokens.length < 2) {
            throw new IllegalArgumentException("Invalid request line");
        }
    }
}
