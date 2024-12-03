package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.example.api.UnicornRequests;
import org.example.api.models.Unicorn;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;


public class UnicornTest {

    @BeforeAll
    public static void setupTests() {
        RestAssured.baseURI = "https://crudcrud.com/api/f59b251782944cebb0dda13c86724c8a";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    public void userShouldBeAbleCreateUnicorn() {
        Unicorn unicorn = Unicorn.builder().name("Galaxy").tailColor("purple").build();
        String id = UnicornRequests.createUnicorn(unicorn);

        UnicornRequests.deleteUnicorn(id);
    }

    @Test
    public void userShouldBeAbleUpdateUnicornTailColor() {
        Unicorn unicorn = Unicorn.builder().name("Magic").tailColor("green").build();

        String id = UnicornRequests.createUnicorn(unicorn);
        String initialName = unicorn.getName();

        String newTailColor = "pink";
        Unicorn updatedColor = Unicorn.builder().tailColor(newTailColor).build();
        String unicornTailJson = null;
        try {
            unicornTailJson = new ObjectMapper().writeValueAsString(updatedColor);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        given()
                .contentType(ContentType.JSON)
                .body(unicornTailJson)
                .when()
                .put("/unicorn/" + id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK)
                .body("name", equalTo(initialName))
                .body("tailColor", equalTo(newTailColor))
                .body("_id", equalTo(id));

        UnicornRequests.deleteUnicorn(id);
    }

    @Test
    public void userShouldBeAbleDeleteExistingUnicorn() {
        Unicorn unicorn = Unicorn.builder().name("Chill").tailColor("yellow").build();
        String id = UnicornRequests.createUnicorn(unicorn);

        UnicornRequests.deleteUnicorn(id);
    }
}
