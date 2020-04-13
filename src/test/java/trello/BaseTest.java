package trello;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class BaseTest {

    protected static final String BASE_URL = "https://api.trello.com/1/";
    protected static final String END_ORGANIZATIONS = "organizations";

    protected static final String KEY = "YOUR_KEY";
    protected static final String TOKEN = "YOUR_TOKEN";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    public static void beforeAll() {
        reqBuilder = new RequestSpecBuilder();

        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);

        reqSpec = reqBuilder.build();
    }
}
