package example.micronaut;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
class TestControllerTest {
    @Test
    void commonV1(@Client("/") HttpClient httpClient) {
        BlockingHttpClient client = httpClient.toBlocking();
        HttpRequest<?> request = createRequest("/common", "1");
        HttpResponse<Map<String, String>> rsp = client.exchange(request, Argument.mapOf(String.class, String.class));
        assertResponse(rsp, "common from v1");

        request = createRequest("/common", null);
        rsp = client.exchange(request, Argument.mapOf(String.class, String.class));
        assertResponse(rsp, "common from v1");

        request = createRequest("/common", "2");
        rsp = client.exchange(request, Argument.mapOf(String.class, String.class));
        assertResponse(rsp, "common from v2");

        request = createRequest("/new", "2");
        rsp = client.exchange(request, Argument.mapOf(String.class, String.class));
        assertResponse(rsp, "new from v2");
    }

    HttpRequest<?> createRequest(String path, String apiVersion) {
        return apiVersion == null ?
                HttpRequest.GET(path) :
                HttpRequest.GET(path).header("X-API-VERSION", apiVersion);
    }

    void assertResponse(HttpResponse<Map<String, String>> rsp, String expected) {
        assertEquals(HttpStatus.OK, rsp.getStatus());
        Map<String, String> body = rsp.body();
        assertNotNull(body);
        assertEquals(1, body.keySet().size());
        assertEquals(expected, body.get("message"));
    }
}
