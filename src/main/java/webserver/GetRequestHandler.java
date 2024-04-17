package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import db.DataBase;
import model.User;
import webserver.constants.HttpHeaders;
import webserver.constants.HttpStatus;

public class GetRequestHandler implements MethodRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetRequestHandler.class);
    static Map<String, Function<HttpRequest, Optional<HttpResponse>>> handlers = new HashMap<>();

    static {
        handlers.put("/user/list.html", request -> {
			try {
				return responseDynamicResource(request.getPath());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
        handlers.put("/user/login.html", request -> {
            if (isUserLoggedIn(request)) {
                return Optional.of(new HttpResponse(HttpStatus.REDIRECT, Map.of(HttpHeaders.LOCATION, "/index.html"), null));
            }
            return Optional.empty();
        });
    }
    @Override
    public Optional<HttpResponse> handle(HttpRequest httpRequest) throws IOException {
        Optional<HttpResponse> response = responseResources(httpRequest);
        if (response.isPresent()) {
            return response;
        }
        return responseGetApi(httpRequest);
    }

    private static Optional<HttpResponse> responseGetApi(HttpRequest httpRequest) {
        // TODO: GET 방식의 요청 URL에 따라 적절한 처리를 하는 부분
        return Optional.empty();
    }

    private Optional<HttpResponse> responseResources(HttpRequest request) throws IOException {
        String path = request.getPath();
        if (path.equals("/")) {
            path = "/index.html";
        }
        Optional<HttpResponse> res = handlers.getOrDefault(path, req -> Optional.empty()).apply(request);
        if (res.isPresent()) {
            return res;
        }
        File file = new File(
            "./src/main/resources" + (path.endsWith(".html") || path.endsWith("favicon.ico")
                ? "/templates" : "/static")
                + path);

        if (file.exists()) {
            byte[] body = Files.readAllBytes(file.toPath());
            String contentType = Files.probeContentType(file.toPath());
            HttpResponse httpResponse = new HttpResponse(HttpStatus.OK,
                Map.of(HttpHeaders.CONTENT_TYPE, contentType != null ? contentType : "null"),
                body);
            return Optional.of(httpResponse);
        }
        return Optional.empty();
    }

    private static boolean isUserLoggedIn(HttpRequest request) {
        if (request.getSessionID().isEmpty()) {
            return false;
        }
        User user = (User) SessionManager.findSession(request.getSessionID()).getAttribute("user");
        return user != null;
    }

    private static Optional<HttpResponse> responseDynamicResource(String path) throws IOException {
        Map<String, List<User>> users = new HashMap<>();
        List<User> userData = new ArrayList<>(DataBase.findAll());
        users.put("users", userData);

        return DynamicPageBuilder.build(path, users);
    }
}
