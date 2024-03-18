package com.example.fitfusion;

import com.example.fitfusion.Post.PersonPost;
import io.restassured.RestAssured;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;


import org.json.JSONException;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import io.restassured.response.Response;
import org.json.JSONObject;

import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Before;

import java.time.LocalDateTime;
import java.util.List;

import static io.restassured.RestAssured.given;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class PostControllerTest {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    // test CRUD operations on person & friendrequests
    @Test
    public void postTest() throws JSONException {

        // POST

//        JSONObject requestBodyPerson = new JSONObject();
//        requestBodyPerson.put("username", "");
//        requestBodyPerson.put("password", "fort_knox");

        JSONObject requestBody = new JSONObject();
        requestBody.put("username", "jakey");
        requestBody.put("password", "fort_knox");

//        JSONObject requestBody = new JSONObject();
//        requestBody.put("title", "first day at gym");
//        requestBody.put("message", "went good");
//        requestBody.put("likes", 0);

        Response response = given().header("Content-Type", "application/json").body(requestBody.toString()).when()
                .post("/people");

        String returnString = response.getBody().asString();
        assertEquals("{\"message\":\"success\"}", returnString);

        // GET
        Response response2 = given().header("Content-Type", "application/json").when()
                .get("/people/@/jakey");
        // System.out.println(response2.asPrettyString()); // to view the response body;

        JSONObject returnObj = new JSONObject(response2.asString());
        assertEquals("fort_knox", returnObj.get("password"));
        assertEquals("jakey", returnObj.get("username"));

        //-------Person Post--------------

        String title = "first day at gym";
        String message = "went good";
        int likes = 0;

        //Post
        JSONObject personPostJSON = new JSONObject();
        personPostJSON.put("title", title);
        personPostJSON.put("message", message);
        personPostJSON.put("likes", likes);
        Response personPostResponse = given().header("Content-Type", "application/json").body(personPostJSON.toString()).when()
                .post("/posts/jakey");

        returnString = personPostResponse.getBody().asString();
        assertEquals("Success!", returnString);

        //Get
        personPostResponse = given().header("Content-Type", "application/json").body(personPostJSON.toString()).when()
                .get("/posts/jakey");

        List<PersonPost> postList = personPostResponse.jsonPath().getList(".", PersonPost.class);
        boolean isPostedObjectInList = postList.stream()
                .anyMatch(post -> post.getTitle().equals(title)
                        && post.getMessage().equals(message)
                        && post.getLikes() == likes
                        && post.getId() == 1);

        assertTrue("Posted object is not in the list", isPostedObjectInList);

        //find by id
        personPostResponse = given().header("Content-Type", "application/json").body(personPostJSON.toString()).when()
                .get("/posts/person/findByID/1");

        String actualTitle = personPostResponse.path("title");
        String actualMessage = personPostResponse.path("message");
        int actualLikes = personPostResponse.path("likes");

        assertEquals(actualTitle, title);
        assertEquals(actualMessage, message);
        assertEquals(actualLikes, likes);

        //like post

        personPostResponse = given().header("Content-Type", "application/json").body(personPostJSON.toString()).when()
                .put("/posts/person/1/like/jakey");

        personPostResponse = given().header("Content-Type", "application/json").body(personPostJSON.toString()).when()
                .get("/posts/person/findByID/1");

        actualLikes = personPostResponse.path("likes");
        assertEquals(actualLikes, likes + 1);

        //check if user liked post

        personPostResponse = given().header("Content-Type", "application/json").body(personPostJSON.toString()).when()
                .get("/posts/person/1/like/jakey");

        //ResponseEntity<String> responseEntity = ResponseEntity.status(HttpStatus.OK).body("liked");
        assertEquals(200, personPostResponse.getStatusCode());
        assertEquals("liked", personPostResponse.getBody().asString());

        //unlike post

        personPostResponse = given().header("Content-Type", "application/json").body(personPostJSON.toString()).when()
                .put("/posts/person/1/unlike/jakey");

        personPostResponse = given().header("Content-Type", "application/json").body(personPostJSON.toString()).when()
                .get("/posts/person/findByID/1");

        actualLikes = personPostResponse.path("likes");
        assertEquals(actualLikes, likes);

        //check if user still liked post

        personPostResponse = given().header("Content-Type", "application/json").body(personPostJSON.toString()).when()
                .get("/posts/person/1/like/jakey");

        //responseEntity = ResponseEntity.status(HttpStatus.OK).body("not liked");
        assertEquals(200, personPostResponse.getStatusCode());
        assertEquals("not liked", personPostResponse.getBody().asString());

        //testing feed

        //creating another user
        requestBody = new JSONObject();
        requestBody.put("username", "tom");
        requestBody.put("password", "fort_snyder");

        response = given().header("Content-Type", "application/json").body(requestBody.toString()).when()
                .post("/people");

        requestBody = new JSONObject();
        requestBody.put("businessName", "State Gym");

        response = given().header("Content-Type", "application/json").body(requestBody.toString()).when()
                .post("/businesses");

        assertEquals("{\"message\":\"success\"}", response.getBody().asString());

        response = given().header("Content-Type", "application/json").body(requestBody.toString()).when()
                .post("/businesses");

    }


}
