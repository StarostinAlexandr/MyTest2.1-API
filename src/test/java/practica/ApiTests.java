package practica;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ApiTests {

    @Test
    public void testWebTourseWithSpec() {
        //Steps.goWebToursWithSpec(Specifications.requestSpec(),Specifications.responseSpec());
        Specifications.installSpec(Specifications.requestSpec(), Specifications.responseSpec());
        Steps.goWebToursWithSpec();
    }


    @Test()
    public void testWebTourse() {
        String sessionID = Steps.goWebTours();
        Steps.getUserData("belltest", "11111", sessionID);
    }


    @Test()
    public void firstTest() {
        given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .log().all()
                .body("page", notNullValue())
                .body("per_page", notNullValue())
                .body("total", notNullValue())
                .body("total_pages", notNullValue())
                .body("data.id", not(hasItem(nullValue())))
                .body("data.first_name", hasItem("Lindsay"));
    }

    @Test
    public void secondTest() {
        Map<String, String> data = new HashMap<>();
        data.put("name", "Kirill");
        data.put("job", "teacher");
        Response responce = given()
                .contentType("application/json")
                .body(data)
                .when()
                .post("https://reqres.in/api/users")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .response();
        JsonPath jsonResponce = responce.jsonPath();
        Assert.assertEquals(jsonResponce.get("name").toString(), data.get("name"), "Name is not valid");
        Assert.assertEquals(jsonResponce.get("job").toString(), data.get("job"), "Job is not valid");
    }

    @Test
    public void loginSuccess() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "eve.holt@reqres.in");
        data.put("password", "pistol");
        Response response = given()
                .contentType("application/json")
                .body(data)
//                .body("email:")
//                .body("eve.holt@reqres.in")
//                .body("password:")
//                .body("12345")
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonResponce = response.jsonPath();
        Assert.assertEquals(jsonResponce.get("token").toString(), "QpwL5tke4Pnpja7X4", "Name is not valid");
//        Assert.assertEquals(jsonResponce.get("job")git .toString(),data.get("job"),"Job is not valid");

    }
}
