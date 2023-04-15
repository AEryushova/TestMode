package data;

import com.github.javafaker.Faker;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.Value;
import org.junit.jupiter.api.BeforeAll;

import java.util.Locale;

import static io.restassured.RestAssured.given;

public class DataGenerator {
    private DataGenerator() {
    }

    private final static Faker faker = new Faker(new Locale("ru"));
    private static RequestSpecification requestSpec = new RequestSpecBuilder()
            .setBaseUri("http://localhost")
            .setPort(9999)
            .setAccept(ContentType.JSON)
            .setContentType(ContentType.JSON)
            .log(LogDetail.ALL)
            .build();

    @BeforeAll
    static void usersRegistration(DataInfo user) {
        given()
                .spec(requestSpec)
                .body(user)
                .when()
                .post("/api/system/users")
                .then()
                .statusCode(200);
    }

    public static String generateLogin() {
        return faker.name().firstName().toLowerCase();
    }

    public static String generatePassword() {
        return faker.internet().password();
    }

    public static class AllUser {

        public static DataInfo addActiveUser() {
            DataInfo user = new DataInfo(generateLogin(), generatePassword(), "active");
            usersRegistration(user);
            return user;
        }

        public static DataInfo addBlockedUser() {
            DataInfo user = new DataInfo(generateLogin(), generatePassword(), "blocked");
            usersRegistration(user);
            return user;
        }

        public static DataInfo notRegisteredUser() {
            DataInfo user = new DataInfo(generateLogin(), generatePassword(), "active");
            return user;
        }

        public static DataInfo invalidLoginUser() {
            DataInfo user = new DataInfo("loginInv", "password", "active");
            usersRegistration(user);
            return new DataInfo(generateLogin(), "password", "active");
        }

        public static DataInfo invalidPasswordUser() {
            DataInfo user = new DataInfo("loginVal", "password", "active");
            usersRegistration(user);
            return new DataInfo("loginVal", generatePassword(), "active");
        }
    }

    @Value
    public static class DataInfo {
        String login;
        String password;
        String status;
    }
}