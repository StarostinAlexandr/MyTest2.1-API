package practica;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

public class Specifications {

    public static RequestSpecification requestSpec(){
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .setBaseUri("https://reqres.in/api/")
                .setContentType("application/json")
                .build();
        return reqSpec;
    }

    public static ResponseSpecification responseSpec(){
        ResponseSpecification resSpec = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build();
        return resSpec;
    }

    public static void installSpec(RequestSpecification requestSpec){
        RestAssured.requestSpecification=requestSpec;
    }

    public static void installSpec(ResponseSpecification responseSpec){
        RestAssured.responseSpecification=responseSpec;
    }

    public static void installSpec(RequestSpecification requestSpec, ResponseSpecification responseSpec){
        RestAssured.requestSpecification = requestSpec;
        RestAssured.responseSpecification = responseSpec;
    }

}
