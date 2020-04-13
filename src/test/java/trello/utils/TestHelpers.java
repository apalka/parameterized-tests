package trello.utils;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class TestHelpers {
    public static void deleteOrganization(String orgId, RequestSpecification reqSpec, final String BASE_URL, final String END_ORGANIZATIONS) {
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + END_ORGANIZATIONS + "/" + orgId)
                .then()
                .statusCode(200);
    }
}
