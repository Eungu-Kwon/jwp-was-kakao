package webserver;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import httpmessage.Header;
import webserver.constants.HttpStatus;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(HttpResponse.class);

    private final String code;
    private final String status;
    private final Header headers;
    private final byte[] body;

    public HttpResponse(HttpStatus httpStatus, Map<String, String> headers, byte[] body) {
        this.code = httpStatus.code;
        this.status = httpStatus.status;
        this.headers = new Header(headers);
        this.body = body;
    }

    public void setCookie(String cookieLine) {
        headers.setCookie(new HttpCookie(cookieLine));
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

    public Header getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
