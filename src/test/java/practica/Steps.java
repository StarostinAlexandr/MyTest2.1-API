package practica;

import io.qameta.allure.Step;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.lang3.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.Assert;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static io.restassured.RestAssured.given;

public class Steps {


    @Step
    public static String goWebToursWithSpec(){
        given()
                .when()
                .post("/WebTours/")
                .then();

        given()
                .when()
                .post("/cgi-bin/welcome.pl?signOff=true")
                .then();

        Response response = given()
                .when()
                .post("/cgi-bin/nav.pl?in=home")
                .then()
                .extract().response();

        Document resultPage = Jsoup.parse(response.asString());

        System.out.println(response.asString());

        String sessionID = resultPage.body().getElementsByAttributeValue("name","userSession").attr("value");

        System.out.println("Session ID: "+ sessionID);

        Assert.assertFalse(sessionID.isEmpty(),"Session is not valid");

        return sessionID;
    }

    @Step
    public static String goWebToursWithSpec(RequestSpecification reqSpec, ResponseSpecification resSpec){
        given()
                .spec(reqSpec)
                .when()
                .post("/WebTours/")
                .then()
                .spec(resSpec);

        given()
                .spec(reqSpec)
                .when()
                .post("/cgi-bin/welcome.pl?signOff=true")
                .then()
                .spec(resSpec);

        Response response = given()
                .spec(reqSpec)
                .when()
                .post("/cgi-bin/nav.pl?in=home")
                .then()
                .spec(resSpec)
                .extract().response();

        Document resultPage = Jsoup.parse(response.asString());

        System.out.println(response.asString());

        String sessionID = resultPage.body().getElementsByAttributeValue("name","userSession").attr("value");

        System.out.println("Session ID: "+ sessionID);

        Assert.assertFalse(sessionID.isEmpty(),"Session is not valid");

        return sessionID;
    }



    @Step
    public static String goWebTours(){
        given()
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post("http://127.0.0.1:1080/WebTours/")
                .then()
                .statusCode(200);

        given()
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post("http://127.0.0.1:1080/cgi-bin/welcome.pl?signOff=true")
                .then()
                .statusCode(200);

        Response response = given()
                .contentType("application/x-www-form-urlencoded")
                .when()
                .post("http://127.0.0.1:1080/cgi-bin/nav.pl?in=home")
                .then()
                .statusCode(200)
                .extract().response();

        Document resultPage = Jsoup.parse(response.asString());

        System.out.println(response.asString());

        String sessionID = resultPage.body().getElementsByAttributeValue("name","userSession").attr("value");

        System.out.println("Session ID: "+ sessionID);

        Assert.assertFalse(sessionID.isEmpty(),"Session is not valid");

        return sessionID;
    }

    @Step
    public  static String getUserData(String login, String password, String sessionID){
        String userData="";
        ExtractableResponse<Response> response = given()
                .contentType("application/x-www-form-urlencoded")
                .body("userSession="+sessionID+"&username="+login+"&password="+password)
                .when()
                .post("http://127.0.0.1:1080/cgi-bin/login.pl")
                .then()
                .statusCode(200)
                .extract();
        System.out.println(response.cookies().toString());
        try{
            userData =
                    URLDecoder.decode(
                            response.cookies().toString(),
                            StandardCharsets.UTF_8.name()
                    );
            System.out.println(userData);
            userData= StringEscapeUtils.unescapeHtml4(userData);
            System.out.println(userData);
        }catch (Exception e){
            e.printStackTrace();
        }
        return userData;
    }

}
