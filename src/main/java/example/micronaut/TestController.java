package example.micronaut;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;

import java.util.Collections;
import java.util.Map;

@Controller
public class TestController {

    public static final String MESSAGE = "message";

    @Get( "/common")
    @Version("1")
    public Map<String, String> commonEndpointV1() {
        return Collections.singletonMap(MESSAGE, "common from v1");
    }

    @Get( "/common")
    @Version("2")
    public Map<String, String> commonEndpointV2() {
        return Collections.singletonMap(MESSAGE, "common from v2");
    }

    @Get( "/new")
    @Version("2")
    public Map<String, String> newEndpointV2() {
        return Collections.singletonMap(MESSAGE, "new from v2");
    }

}
