package webserver;

import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import httpmessage.Body;
import httpmessage.Header;
import httpmessage.RequestLine;
import webserver.constants.HttpMethods;

public class HttpRequest {
    private final Header header;
    private final HttpCookie cookie;
    private final RequestLine requestLine;
    private final Body body;
    private final Map<String, String> form;

    public HttpRequest(String requestLine, Map<String, String> header, String body) {
        this.requestLine = new RequestLine(requestLine);
        this.header = new Header(header);
        this.cookie = new HttpCookie(header.get("Cookie"));
        this.body = new Body(body);
        this.form = parseForm();
    }

    public boolean isMethod(String method) {
        return requestLine.getMethod().equals(method);
    }

    public String getPath() {
        return requestLine.getPath();
    }

    public Map<String, String> getForm() {
        return form;
    }

    public Map<String, String> getQuery() {
        return requestLine.getQueryParam();
    }

    public HttpMethods getMethod() {
        return requestLine.getMethod();
    }

    private Map<String, String> parseForm() {
        return body == null ? new HashMap<>() : parseKeyValuePairs(body.getBody());
    }

    public static Map<String, String> parseKeyValuePairs(String input) {
        String[] tokens = input.split("&");
        Map<String, String> map = new HashMap<>();
        for (String token : tokens) {
            List<String> keyValue = Arrays.stream(token.split("=", 2))
                .map(String::trim)
                .map(HttpRequest::decodeUrl)
                .collect(Collectors.toList());

            addKeyValuePairIfValid(keyValue, map);
        }
        return map;
    }

    private static String decodeUrl(String s) {
        return URLDecoder.decode(s, java.nio.charset.StandardCharsets.UTF_8);
    }

    private static void addKeyValuePairIfValid(List<String> keyValue, Map<String, String> map) {
        if (keyValue.size() > 1) {
            map.put(keyValue.get(0), keyValue.get(1));
        }
    }
}
