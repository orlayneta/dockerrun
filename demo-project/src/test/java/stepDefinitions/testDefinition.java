package stepDefinitions;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.junit.runner.RunWith;

import cucumber.api.DataTable;
import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.junit.Cucumber;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import resources.TestDatabuild;

@RunWith(Cucumber.class)
public class testDefinition {

    private RequestSpecification req = null;
    private ResponseSpecification resspec = null;
    private RequestSpecification res = null;
    private Response response = null;
    private String idStudent = null;

    private TestDatabuild data = new TestDatabuild();

    @After
    public void removeStudent() {

	Response responseDelete = res.delete("/student/" + idStudent);
	System.out.println(responseDelete);
    }

    @Given("^Add Student Payload with name \"([^\"]*)\"$")
    public void add_Student_Payload_with_name(String name) throws MalformedURLException, URISyntaxException {

	String link = "http://172.16.232.1";
	URL url = new URL(link);
	URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(),
		url.getQuery(), url.getRef());
	url = uri.toURL();

	RestAssured.baseURI = url.toString();
	RestAssured.port = 8081;
	req = new RequestSpecBuilder().setBaseUri(RestAssured.baseURI).setContentType(ContentType.JSON).build();

	resspec = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
	res = given().spec(req).log().all().body(data.addStudentPayload(name));
    }

    @When("^user calls Post htpp request$")
    public void user_calls_Post_htpp_request() throws Throwable {
	response = res.when().post("/student").then().log().all().spec(resspec).extract().response();
	fillBodyResponse(response);
    }

    @Then("^the API call is success with status code (\\d+)$")
    public void the_API_call_is_success_with_status_code(int arg1) {
	assertEquals(200, response.getStatusCode());

    }

    @Then("^<field> in response body is <value>$")
    public void field_in_response_body_is_value(DataTable dt) throws Throwable {
	List<Map<String, String>> list = dt.asMaps(String.class, String.class);
	String res = response.asString();
	JsonPath js = new JsonPath(res);
	for (int i = 0; i < list.size(); i++) {
	    assertEquals(js.get("name"), list.get(i).get("name"));
	    assertEquals(js.get("registration"), list.get(i).get("registration"));
	}
    }

    private void fillBodyResponse(Response response) {
	String bodyResponse = response.getBody().asString();
	JsonPath js = JsonPath.from(bodyResponse);
	idStudent = js.get("id");

    }

}
