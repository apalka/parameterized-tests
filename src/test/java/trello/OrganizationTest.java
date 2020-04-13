package trello;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static trello.utils.TestHelpers.deleteOrganization;

public class OrganizationTest extends BaseTest {

    private static List<String> orgIds;
    private static final String defaultDisplayName = "Display name";
    private static final String defaultDesc = "Some description, test.";

    private static Stream<Arguments> createOrganizationData() {
        return Stream.of(
                Arguments.of(defaultDisplayName, defaultDesc, "olatest", "https://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "testola", "http://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "ola", "https://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "ola_test", "https://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "olatest123", "https://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "olatest_123", "https://olatest.pl"));
    }

    private static Stream<Arguments> createOrganizationInvalidData() {
        return Stream.of(
                Arguments.of("", defaultDesc, "olatest", "https://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "ol", "https://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "OLATEST_123", "https://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "ola test", "https://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "ola.test", "https://olatest.pl"),
                Arguments.of(defaultDisplayName, defaultDesc, "olatest_123", "www.olatest.pl"));
    }

    @BeforeAll
    public static void setUp() {
        BaseTest.beforeAll();
        orgIds = new ArrayList<String>();
    }

    @DisplayName("Create organization with valid data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("createOrganizationData")
    public void createOrganization(String displayName, String desc, String name, String website) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + END_ORGANIZATIONS)
                .then()
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        if (response.getStatusCode() == 200) {
            orgIds.add(json.getString("id"));
        }

        Assertions.assertThat(response.getStatusCode()).isEqualTo(200);
        Assertions.assertThat(json.getString("displayName")).isEqualTo(displayName);
        Assertions.assertThat(json.getString("desc")).isEqualTo(desc);
        Assertions.assertThat(json.getString("name")).isEqualTo(name);
        Assertions.assertThat(json.getString("website")).isEqualTo(website);
    }

    @DisplayName("Create organization with invalid data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("createOrganizationInvalidData")
    public void createOrganizationWithInvalidData(String displayName, String desc, String name, String website) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + END_ORGANIZATIONS)
                .then()
                .extract()
                .response();

        JsonPath json = response.jsonPath();

        if (response.getStatusCode() == 200) {
            orgIds.add(json.getString("id"));
        }

        Assertions.assertThat(response.getStatusCode()).isEqualTo(400);

    }

    @AfterAll
    public static void deleteOrganizations() {
        for (String org : orgIds) {
            deleteOrganization(org, reqSpec, BASE_URL, END_ORGANIZATIONS);
        }
    }
}
