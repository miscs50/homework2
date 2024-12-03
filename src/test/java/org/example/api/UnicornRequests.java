package org.example.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import org.apache.http.HttpStatus;
import org.example.api.models.Unicorn;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

public class UnicornRequests {

    public static String createUnicorn(Unicorn unicorn) {

        String unicornJson = null;
        try {
            unicornJson = new ObjectMapper().writeValueAsString(unicorn);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return given()
                .contentType(ContentType.JSON)
                .body(unicornJson)
                .when()
                .post("/unicorn")
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_CREATED)
                .body("$", hasKey("name"))
                .body("$", hasKey("tailColor"))
                .body("$", hasKey("_id"))
                .body("name", equalTo("Galaxy"))
                .body("tailColor", equalTo("purple"))
                .extract().as(Unicorn.class, ObjectMapperType.GSON).getId();
    }

    public static void deleteUnicorn(String id) {
        given()
                .delete("/unicorn/" + id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NO_CONTENT);
        given()
                .get("/unicorn/" + id)
                .then()
                .assertThat()
                .statusCode(HttpStatus.SC_NOT_FOUND);
    }
}
