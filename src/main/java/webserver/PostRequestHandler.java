package webserver;

import db.DataBase;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.constants.HttpHeaders;
import webserver.constants.HttpStatus;

public class PostRequestHandler implements MethodRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetRequestHandler.class);

    @Override
    public Optional<HttpResponse> handle(HttpRequest httpRequest) throws IOException {
        if (httpRequest.getPath().equals("/user/create")) {
            return createAccount(httpRequest);
        }

        if (httpRequest.getPath().equals("/user/login")) {
            return login(httpRequest.getForm());
        }

        return Optional.empty();
    }

    private static Optional<HttpResponse> createAccount(HttpRequest httpRequest) {
        createUser(httpRequest.getForm());
        HttpResponse httpResponse = new HttpResponse(HttpStatus.REDIRECT,
            Map.of(HttpHeaders.LOCATION, "/index.html"), null);
        return Optional.of(httpResponse);
    }

    private Optional<HttpResponse> login(Map<String, String> query) {
        User user = DataBase.findUserById(query.get("userId"));
        if (user == null || !user.getPassword().equals(query.get("password"))) {
            return Optional.of(new HttpResponse(HttpStatus.REDIRECT, Map.of(HttpHeaders.LOCATION, "/user/login_failed.html"), null));
        }
        logger.debug("User Login : {}", query.get("userId"));
        HttpResponse httpResponse = new HttpResponse(HttpStatus.REDIRECT, Map.of(HttpHeaders.LOCATION, "/index.html"), null);
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId);
        session.setAttribute("user", user);
        httpResponse.setCookie("JSESSIONID=" + sessionId + ";logined=true");
        SessionManager.add(session);
        return Optional.of(httpResponse);
    }

    private static void createUser(Map<String, String> querys) {
        DataBase.addUser(
            new User(querys.get("userId"), querys.get("password"), querys.get("name"),
                querys.get("email")));
        logger.debug("User Create : {}", querys.get("userId"));
    }
}
