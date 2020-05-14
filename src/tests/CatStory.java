package tests;

import Solution.Given;
import Solution.Then;
import Solution.When;
import org.junit.Assert;

public class CatStory {


        protected Cat cat;
        @Given("a cat of age &age")
        public void aCat(Integer age) {
            cat = new Cat(age);
        }



        @Given("a cat of age &age and name &name")
        public void aCatWithName(Integer age , String name) {
            cat = new Cat(age,name);
        }

        @When("the cat is not taken out for a walk, the number of hours is &hours")
        public void catNotTakenForAWalk(Integer hours) {
            cat.notTakenForAWalk(hours);
        }

        @Then("the house condition is &condition")
        public void theHouseCondition(String condition) {
            Assert.assertEquals(condition, cat.houseCondition());
        }




}
