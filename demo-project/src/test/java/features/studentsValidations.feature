Feature: Validating students API's

Scenario: Verify if student is being Succesfully added
	Given Add Student Payload with name "Joe"
	When user calls Post htpp request
	Then the API call is success with status code 200
	And <field> in response body is <value>
	|name   | 
	|Joe 	| 
