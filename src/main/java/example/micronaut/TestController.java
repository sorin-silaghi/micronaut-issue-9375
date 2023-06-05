package example.micronaut;

import io.micronaut.core.version.annotation.Version;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;

@Controller("/")
public class TestController {

    @Post(value = "common")
    @Version("1")
    @Produces(MediaType.TEXT_PLAIN)
    public String commonEndpointV1() {
        return "This endpoint exists both in V1 and V2";
    }

    @Post(value = "common")
    @Version("2")
    @Produces(MediaType.TEXT_PLAIN)
    public String commonEndpointV2() {
        return "This endpoint exists both in V1 and V2";
    }

    @Post(value = "new")
    @Version("2")
    @Produces(MediaType.TEXT_PLAIN)
    public String newEndpointV2() {
        return "This is a new endpoint in V2 of the API";
    }

}
