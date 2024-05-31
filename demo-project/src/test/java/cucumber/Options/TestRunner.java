package cucumber.Options;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/java/features", glue = "stepDefinitions", plugin = { "pretty",
	"json:target/surefire-reports/cucumber.json" }, monochrome = true)
public class TestRunner {

}