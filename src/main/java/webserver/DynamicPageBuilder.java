package webserver;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.io.ClassPathTemplateLoader;
import com.github.jknack.handlebars.io.TemplateLoader;

import webserver.constants.HttpHeaders;
import webserver.constants.HttpStatus;

public class DynamicPageBuilder {
	public static Optional<HttpResponse> build(String location, Object data) throws IOException {
		TemplateLoader loader = new ClassPathTemplateLoader();
		loader.setPrefix("/templates");
		setSuffix(loader, location);
		Handlebars handlebars = new Handlebars(loader);
		handlebars.registerHelper("plus", new Helper<Integer>() {
			@Override
			public Object apply(Integer context, Options options) throws IOException {
				return context + 1;
			}
		});
		Template template = handlebars.compile(location);

		return Optional.of(new HttpResponse(HttpStatus.OK, Map.of(HttpHeaders.CONTENT_TYPE, "text/html"),
			template.apply(data).getBytes()));
	}

	private static void setSuffix(TemplateLoader loader, String location) {
		if (location.endsWith(".html")) {
			loader.setSuffix("");
			return;
		}
		loader.setSuffix(".html");
	}
}
