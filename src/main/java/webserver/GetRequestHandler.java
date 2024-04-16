package webserver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import db.DataBase;
import model.User;
import webserver.constants.HttpHeaders;
import webserver.constants.HttpStatus;

public class GetRequestHandler implements MethodRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(GetRequestHandler.class);

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
        if (path.startsWith("/user/list")) {
            return responseDynamicResource(path);
        }
        if (path.startsWith("/user/login") && !request.getSessionID().isEmpty()) {
            return Optional.of(new HttpResponse(HttpStatus.REDIRECT, Map.of(HttpHeaders.LOCATION, "/index.html"), null));
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

    private Optional<HttpResponse> responseDynamicResource(String path) throws IOException {
        TemplateLoader loader = new ClassPathTemplateLoader();
        loader.setPrefix("/templates");
        loader.setSuffix(".html");
        Handlebars handlebars = new Handlebars(loader);
        handlebars.registerHelper("plus", new Helper<Integer>() {
            @Override
            public Object apply(Integer context, Options options) throws IOException {
                return context + 1;
            }
        });
        Template template = handlebars.compile("user/list");

        Map<String, List<User>> users = new HashMap<>();
		List<User> userData = new ArrayList<>(DataBase.findAll());
        users.put("users", userData);
        return Optional.of(new HttpResponse(HttpStatus.OK, Map.of(HttpHeaders.CONTENT_TYPE, "text/html"),
            template.apply(users).getBytes()));
    }
}
