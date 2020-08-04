package practica;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static practica.Specifications.*;

public class ApiTests {

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
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .log().all()
                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("token").toString(), "QpwL5tke4Pnpja7X4", "Name is not valid");

    }

    @Test
    public void loginSuccessSpec() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");
        Response response = given()
//                .contentType("application/json")
                .spec(requestSpec())
                .body(body)
                .when()
                .post("login")
//                .post("https://reqres.in/api/login")
                .then()
                .spec(responseSpec())
                .log().all()
//                .statusCode(200)
                .extract()
                .response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("token").toString(), "QpwL5tke4Pnpja7X4", "Name is not valid");

    }

    @Test
    public void loginFailSpec() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "");
        Response response = given()
//                .contentType("application/json")
                .spec(requestSpec())
                .body(body)
                .when()
                .post("login")
//                .post("https://reqres.in/api/login")
                .then()
//                .spec(responseSpec())
                .log().all()
                .statusCode(400)
                .extract()
                .response();
//        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(response.statusCode(), 400, "Запрос на логин Пользователя без ввода пароля не имеет статус BAD_REQUEST (400) ");

    }

    @Test
    public void getUsersSpec() {
        Map<Integer, String> usersAvatar = new HashMap<>();
        Response response = given()
                .contentType("application/json")
                .spec(requestSpec())
                .when()
                .get("users?page=2")
                .then()
                .spec(responseSpec())
                .log().all()
                .extract()
                .response();
        JsonPath jsonResponse = response.jsonPath();
        List list = new ArrayList<Object>();
       list= jsonResponse.getList("data.avatar");
        list.stream().forEach(x-> System.out.println(StringUtils.endsWithIgnoreCase(x.toString(), "/128.jpg")) );
//        System.out.println(StringUtils.endsWithIgnoreCase(list.forEach(), "/128.jpg"));
        response.jsonPath().getList("data.avatar").forEach(x-> Assert.assertTrue(StringUtils.endsWithIgnoreCase(x.toString(), "/128.jpg"), "Не у всех пользователей имена аватаров совпадают"));
    }
}
