import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.testng.Assert;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
public class PlaceApiTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// Validate add place API is working as expected.
		
		// Step 1 - Add a new place
		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{ \r\n"
				+ "\"location\": { \r\n"
				+ "\"lat\": -38.383494, \r\n"
				+ "\"lng\": 33.427362 \r\n"
				+ "}, \r\n"
				+ "\"accuracy\": 50, \r\n"
				+ "\"name\": \"Colony house\", \r\n"
				+ "\"phone_number\": \"(+91) 983 893 3937\", \r\n"
				+ "\"address\": \"Fromville\", \r\n"
				+ "\"types\": [ \r\n"
				+ "\"shoe park\", \r\n"
				+ "\"shop\" \r\n"
				+ "], \r\n"
				+ "\"website\": \"http://rahulshettyacademy.com\", \r\n"
				+ "\"language\": \"French-IN\" \r\n"
				+ "}").when().post("maps/api/place/add/json")
				.then().assertThat().statusCode(200).body("scope",equalTo("APP")).extract().response().asString();
		
		System.out.println(response);
		
		JsonPath js = new JsonPath(response);
		String placeID = js.getString("place_id");
		System.out.println(placeID);
		
		// Step 2 - Update the address
		
		String updated_address = "Alberquerque";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "    \"place_id\": \""+placeID+"\",\r\n"
				+ "    \"address\": \""+updated_address+"\",\r\n"
				+ "    \"key\":\"qaclick123\"\r\n"
				+ "}").when().put("maps/api/place/update/json")
		.then().assertThat().log().all().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		System.out.println(placeID);
		
		// Step 3 - Verify the address was updated
		
		String Get_resp=given().log().all().queryParam("key","qaclick123").queryParam("place_id",placeID).when().get("maps/api/place/get/json")
		.then().assertThat().log().all().statusCode(200).extract().response().asString();
		
		System.out.println(Get_resp);
		
		JsonPath js2 = Payload.rawtoJson(Get_resp);
		
		String Actual_address = js2.getString("address");
		
		System.out.println(Actual_address);
		
		//Junit (or) TestNG
		Assert.assertEquals(Actual_address, updated_address);
	
	}

}