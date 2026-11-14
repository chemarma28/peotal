package support;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class E2ETest {
    protected final HttpClient client = HttpClient.newHttpClient();
    protected final Gson gson = new GsonBuilder().create();

    protected HttpRequest post(String serverPath, String url, Object body) {
        return HttpRequest
                .newBuilder(URI.create((serverPath.endsWith("/") ? serverPath : serverPath + "/") + url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(body)))
                .build();
    }
}
