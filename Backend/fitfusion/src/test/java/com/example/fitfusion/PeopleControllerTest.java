package com.example.fitfusion;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.jsonpath.JsonPath;

import io.restassured.RestAssured;
import io.restassured.internal.path.json.mapping.JsonObjectDeserializer;
import io.restassured.response.Response;
import org.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class PeopleControllerTest {
        @LocalServerPort
        int port;

        @Before
        public void setUp() {
                RestAssured.port = port;
                RestAssured.baseURI = "http://localhost";
        }

        // test CRUD operations on person & friendrequests
        @Test
        public void personTest() throws JSONException {

                // POST

                JSONObject requestBody = new JSONObject();
                requestBody.put("username", "jerry");
                requestBody.put("password", "fort_knox");

                Response response = given().header("Content-Type", "application/json").body(requestBody.toString())
                                .when()
                                .post("/people");

                String returnString = response.getBody().asString();
                //assertEquals("{\"message\":\"success\"}", returnString);

                // GET
                Response response2 = given().header("Content-Type", "application/json").when()
                                .get("/people/@/jerry");
                // System.out.println(response2.asPrettyString()); // to view the response body;

                JSONObject returnObj = new JSONObject(response2.asString());
                assertEquals("fort_knox", returnObj.get("password"));
                assertEquals("jerry", returnObj.get("username"));

                // friend requests

                JSONObject requestBody2 = new JSONObject();
                requestBody2.put("username", "tom");

                given().header("Content-Type", "application/json").body(requestBody2.toString()).when()
                                .post("/people");

                // 3) jerry sends a friend request to tom

                Response response3 = given().header("Content-Type", "application/json").when()
                                .post("/people/@/jerry/friend/tom");
                String returnString3 = response3.getBody().asString();

                assertEquals("Created new friend request from jerry to tom.", returnString3);

                // 4) try sending a duplicate friend request

                Response response4 = given().header("Content-Type", "application/json").when()
                                .post("/people/@/jerry/friend/tom");
                String returnString4 = response4.getBody().asString();

                assertEquals("jerry already has a pending friend request to tom!", returnString4);

                // 5) tom implictly accepts the friend request by sending one of his own

                Response response5 = given().header("Content-Type", "application/json").when()
                                .post("/people/@/tom/friend/jerry");
                String returnString5 = response5.getBody().asString();

                assertEquals("tom is now friends with jerry!", returnString5);

                // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                // test challenges
                // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

                // 6 tom challenges jerry

                Response response6 = given().header("Content-Type", "application/json").when()
                                .post("/people/@/tom/challenge/jerry");
                // String returnString6 = response6.getBody().asString();

                assertEquals(201, response6.getStatusCode());

                // 7 test get all challenges

                Response response7 = given().header("Content-Type", "application/json").when()
                                .get("challenges");

                JSONArray returnObj7 = new JSONArray(response7.asString());
                JSONObject obj7 = (JSONObject) returnObj7.get(0);
                assertEquals("PENDING", obj7.get("status"));

                // 8

                Response response8 = given().header("Content-Type", "application/json").when()
                                .get("challenges/1");
                JSONObject return8 = new JSONObject(response8.asString());
                JSONObject challenger = (JSONObject) return8.get("challenger");
                JSONObject challengee = (JSONObject) return8.get("challengee");

                assertEquals("tom", challenger.get("username"));
                assertEquals("jerry", challengee.get("username"));

                // 9 test get from and get to username

                Response response9a = given().header("Content-Type", "application/json").when()
                                .get("challenges/from/tom");

                Response response9b = given().header("Content-Type", "application/json").when()
                                .get("challenges/from/tom");

                assertEquals(response9a.asString(), response9b.asString());

                // 10 decline a challenge

                Response response10 = given().header("Content-Type", "application/json").when()
                                .delete("challenges/1/decline");

                assertEquals(204, response10.getStatusCode());

                // 11 accept a challenge

                Response response11b = given().header("Content-Type", "application/json").when()
                                .post("/people/@/tom/challenge/jerry");

                System.out.println(response11b.asPrettyString());

                Response response11 = given().header("Content-Type", "application/json").when()
                                .put("challenges/2/accept");

                assertEquals(201, response11.getStatusCode());

                // 12 complete a challenge

                Response response12 = given().header("Content-Type", "application/json").when()
                                .delete("challenges/2/complete");

                assertEquals(204, response12.getStatusCode());

                // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~``
                // test workout sesssions

                // 13 first, create a new workout

                JSONObject workout = new JSONObject();
                requestBody.put("name", "pushup");
                requestBody.put("reps", 100);

                // 13b instantiate a new workout session for tom

                Response response13 = given().header("Content-Type", "application/json")
                                .when()
                                .post("/wsessions/tom/2023/12/3/new");
                assertEquals("success", response13.asString());

                // 14 test can't create two sessions on same day

                Response response14 = given().header("Content-Type", "application/json")
                                .when()
                                .post("/wsessions/tom/2023/12/3/new");
                assertEquals("tom already has a workout session on 12/3/2023", response14.asString());

                // 15 try adding a workout to a person's workout session

                Response response15 = given().header("Content-Type", "application/json").body(workout.toString())
                                .when()
                                .post("/wsessions/tom/2023/12/3");
                assertEquals("Added workout to tom on 12/3/2023", response15.asString());

                // 16 verify the workout session was initialized

                Response response16 = given().header("Content-Type", "application/json").body(workout.toString())
                                .when()
                                .get("/wsessions/tom/2023/12/3");
                JSONObject return16 = new JSONObject(response16.asString());
                assertEquals(1, return16.get("id"));

                // 17 verify the /wsessions/username endpoint works as intended

                Response response17 = given().header("Content-Type", "application/json").body(workout.toString())
                                .when()
                                .get("/wsessions/tom");

                JSONArray return17 = new JSONArray(response17.asString());
                assertEquals(1, return17.length());

                // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~```
                // test admin functions
                // 18 ban jerry

                Response response18 = given().header("Content-Type", "application/json")
                                .when()
                                .post("/people/@/jerry/ban");

                assertEquals("banned jerry", response18.asString());

                // 19 jerry should be banned
                Response response19 = given().header("Content-Type", "application/json")
                                .when()
                                .get("/people/@/jerry/isBanned");
                assertEquals("true", response19.asString());

                // 20 unban jerry

                Response response20 = given().header("Content-Type", "application/json")
                                .when()
                                .post("/people/@/jerry/unban");
                assertEquals("unbanned jerry", response20.asString());

                // ~~~~~~~~~~~~~~~~~~~
                // test business banning

                // 21 create a new business

                JSONObject business = new JSONObject();
                business.put("businessName", "sohio");
                business.put("city", "cleveland");

                Response response21 = given().header("Content-Type", "application/json").body(business.toString())
                                .when()
                                .post("/businesses");
                assertEquals("{\"message\":\"success\"}", response21.asString());

                System.out.println(response21.asPrettyString());

                // 21b get this business

                Response response21b = given().header("Content-Type", "application/json")
                                .when()
                                .get("/businesses/@/sohio");

                System.out.println(response21b.asPrettyString());
                JSONObject return21b = new JSONObject(response21b.asString());
                assertEquals("sohio", return21b.get("businessName"));

                // 22 ban this business

                Response response22 = given().header("Content-Type", "application/json")
                                .when()
                                .post("/businesses/@/sohio/ban");

                assertEquals("banned sohio", response22.asString());

                // 23 it should be banned

                Response response23 = given().header("Content-Type", "application/json")
                                .when()
                                .get("/businesses/@/sohio/isBanned");

                assertEquals("true", response23.asString());

                // 24 unban this business

                Response response24 = given().header("Content-Type", "application/json")
                                .when()
                                .post("/businesses/@/sohio/unban");

                assertEquals("unbanned sohio", response24.asString());

                //
                // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                // test adding workouts & routines

                // 25 add legday to jerry

                JSONObject routine = new JSONObject();
                routine.put("name", "legday");

                Response response25 = given().header("Content-Type", "application/json").body(routine.toString())
                                .when()
                                .post("/user/jerry/routines");
                assertEquals("{\"message\":\"success\"}", response25.asString());

                // 26 verify jerry has legday

                Response response26 = given().header("Content-Type", "application/json").body(routine.toString())
                                .when()
                                .get("/user/jerry/routines");
                JSONArray return26 = new JSONArray(response26.asString());
                JSONObject obj26 = (JSONObject) return26.get(0);
                assertEquals("legday", obj26.get("name"));

                // 27 verify that /routines works as intended

                Response response27 = given().header("Content-Type", "application/json").body(routine.toString())
                                .when()
                                .get("/routines");
                JSONArray return27 = new JSONArray(response27.asString());
                assertEquals(return26.length(), return27.length());

                // 28 try adding squat to legday

                JSONObject squat = new JSONObject();
                squat.put("name", "squat");
                squat.put("reps", 100);

                Response response28 = given().header("Content-Type", "application/json").body(squat.toString())
                                .when()
                                .post("/routine/1/workout");

                assertEquals("{\"message\":\"success\"}", response28.asString());

                // 29 verify legday has squat

                Response response29 = given().header("Content-Type", "application/json")
                                .when()
                                .get("/routine/1/workout");

                System.out.println(response29.asPrettyString());

                // 30 test set home gym

                Response response30 = given().header("Content-Type", "application/json")
                                .when()
                                .post("/people/@/tom/setHome/sohio");

                assertEquals("Home gym successfully added to tom", response30.asString());

                // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
                // test person search
                //
                //jerry > legday > squat, rdl, dl 
                //tom > legday > squat, rdl 
                //tommy 

                //31 jerry should not have any friend recommendations with very high strictness  

                Response response31 = given().header("Content-Type", "application/json")
                                .when()
                                .get("/people/@/jerry/recommend/5");

                JSONArray return31 = new JSONArray(response31.asString());
                assertEquals(0, return31.length());

                //32 global search for "t" should yield tom 

                Response response32 = given().header("Content-Type", "application/json")
                                .when()
                                .get("/people/search/t");

                System.out.println(response32.asPrettyString());
                JSONArray return32 = new JSONArray(response32.asString());
                JSONObject obj32 = (JSONObject) return32.get(0);
                assertEquals("tom", obj32.get("username"));

                //33 friend search should be empty as jerry and tom are already friends 

                Response response33 = given().header("Content-Type", "application/json")
                                .when()
                                .get("/people/@/jerry/search/tom");

                JSONArray return33 = new JSONArray(response33.asString());
                assertEquals(0, return33.length());

                //34 get all of jerry's workouts 

                Response response34 = given().header("Content-Type", "application/json")
                                .when()
                                .get("/people/@/jerry/workouts");

                JSONArray return34 = new JSONArray(response34.asString());

                assertEquals(1, return34.length());
        }
}
