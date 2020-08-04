package practica;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static practica.Specifications.requestSpec;
import static practica.Specifications.responseSpec;

public class ApiTests {

    @Test
    public void loginSuccess() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "pistol");
        Response response = given()
                .spec(requestSpec())
                .body(body)
                .when()
                .post("login")
                .then()
                .spec(responseSpec())
                .log().all()
                .extract()
                .response();
        JsonPath jsonResponse = response.jsonPath();
        Assert.assertEquals(jsonResponse.get("token").toString(), "QpwL5tke4Pnpja7X4", "Token is not valid");
    }

    @Test
    public void loginFail() {
        Map<String, String> body = new HashMap<>();
        body.put("email", "eve.holt@reqres.in");
        body.put("password", "");
        Response response = given()
                .spec(requestSpec())
                .body(body)
                .when()
                .post("login")
                .then()
                .log().all()
                .statusCode(400)
                .extract()
                .response();
        Assert.assertEquals(response.statusCode(), 400, "Запрос на логин Пользователя без ввода пароля не имеет статус BAD_REQUEST (400) ");
    }

    @Test
    public void getUsersAvatarHasTheSameName() {
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
        response.jsonPath().getList("data.avatar").forEach(x-> Assert.assertTrue(StringUtils.endsWithIgnoreCase(x.toString(), "/128.jpg"), "Не у всех пользователей имена аватаров совпадают"));
    }

    @Test
    public void getListResourceCheckSorted() {
        Map<Integer, String> usersAvatar = new HashMap<>();
        Response response = given()
                .spec(requestSpec())
                .when()
                .get("unknown")
                .then()
                .spec(responseSpec())
                .log().all()
                .extract()
                .response();
        JsonPath jsonPath = response.jsonPath();
        jsonPath.getList("data.year").stream().mapToInt(x-> Integer.parseInt(x.toString())).reduce( (p, n) ->{Assert.assertTrue(n >= p, "Список не отсортирован по годам"); return 0;});
    }
}
