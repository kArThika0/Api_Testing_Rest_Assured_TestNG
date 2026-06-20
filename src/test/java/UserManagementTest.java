import UserManagementPOM.UserPOJO;
import com.github.tomakehurst.wiremock.WireMockServer;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class UserManagementTest {

/*
request- POST /api/users
{
  "name": "John",
  "email": "john@test.com"
}
response-
{
  "userId": "USR123",
  "name": "John",
  "email": "john@test.com",
  "status": "CREATED"
}
*/
    public WireMockServer wireMockServer;

    @BeforeSuite
    public void startServer(){
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();


    }


    @Test (groups = {"positive"})
    public void createUser(){

        //settingup wiremock stub
        wireMockServer.stubFor(
                post(urlEqualTo("/api/users"))
                        .willReturn(aResponse().withStatus(201)
                        .withHeader(
                                "Content-Type",
                                "application/json")

                        .withBody("""
                                {
                                  "userId":"USR123",
                                  "name":"Karthika",
                                  "email":"abc@gmail.com",
                                  "status": "CREATED"
                               
                                }
                                """)
                )
        );



        RestAssured.baseURI="http://localhost:8089";
        //constructing body message
        UserPOJO request=new UserPOJO();
        request.setName("Karthika");
        request.setEmail("abc@gmail.com");
        //sending body message
        Response createUserResponse= given().header("Content-Type","application/json").body(request)
                .when().post("/api/users").then().assertThat().statusCode(201).body(matchesJsonSchemaInClasspath("schemas/userManagement.json")).log().all().extract().response();
        UserPOJO userDetails=createUserResponse.as(UserPOJO.class);
        System.out.println(userDetails.getUserId());
    }

    @Test(groups = {"negative"})
    public void createUserError(){
        wireMockServer.stubFor(
                post(urlEqualTo("/api/users"))
                        .withRequestBody(
                                matchingJsonPath("$.email",
                                        absent()))
                        .willReturn(
                                aResponse()
                                        .withStatus(400)
                                        .withBody("""
                                        {
                                          "error":"EMAIL_REQUIRED"
                                        }
                                        """)
                        )
        );
        RestAssured.baseURI="http://localhost:8089";
        //constructing body message
        UserPOJO request=new UserPOJO();
        request.setName("Karthika");

        //sending body message with wrong data
        Response createUserResponse= given().header("Content-Type","application/json").body(request)
                .when().post("/api/users").then().statusCode(400).log().all().extract().response();
        String error =
               createUserResponse.jsonPath()
                        .getString("error");

        Assert.assertEquals(error, "EMAIL_REQUIRED");
    }





    @AfterSuite
    public void stopServer(){
        wireMockServer.stop();
    }
}
