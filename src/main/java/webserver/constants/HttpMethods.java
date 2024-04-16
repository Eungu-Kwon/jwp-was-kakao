package webserver.constants;

public enum HttpMethods {

    // public static String GET = "GET";
    // public static String POST = "POST";
    // public static String PUT = "PUT";
    // public static String DELETE = "DELETE";

    GET("GET"),
    POST("POST"),
    PUT("PUT"),
    DELETE("DELETE");

    private final String method;

    HttpMethods(String method) {
        this.method = method;
    }

    public boolean equals(String method) {
        return this.method.equals(method);
    }
}
