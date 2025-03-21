import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import tqs.cucumber.Calculator;

public class CalculatorSteps {
    
    private Calculator calc;
    private Exception thrownException;

    @Given("a calculator I just turned on")
    public void setup() {
        calc = new Calculator();
    }

    @When("I add {int} and {int}")
    public void add(int arg1, int arg2) {
        calc.push(arg1);
        calc.push(arg2);
        calc.push("+");
    }

    @When("I subtract {int} by {int}")
    public void subtract(int arg1, int arg2) {
        calc.push(arg1);
        calc.push(arg2);
        calc.push("-");
    }
    
    @When("I multiply {int} by {int}")
    public void multiply(int arg1, int arg2) {
        calc.push(arg1);
        calc.push(arg2);
        calc.push("*");
    }

    @When("I try to use the operator {string}")
    public void use_invalid_operator(String inv_op) {
        try {
            calc.push(inv_op);
        } catch (Exception e) {
            thrownException = e;
        }
    }

    @Then("the result should be {int}") 
    public void result_is(double expected) {
        Number value = calc.value();
        assertEquals(value, expected);
    }

    @Then("an exception should be thrown")
    public void throw_exception() {
        assertNotNull(thrownException);
    }


}
