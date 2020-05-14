package tests;

import Solution.Given;
import Solution.Then;
import Solution.When;
import org.junit.Assert;
import org.junit.ComparisonFailure;

public class CatStory {


        protected Cat cat;
        @Given("a Cat of age &age")
        public void aCat(Integer age) {
            cat = new Cat(age);
        }



        @Given("a Cat of age &age and name &name")
        public void aCatWithName(Integer age , String name) {
            cat = new Cat(age,name);
        }

        @When("the Cat is not taken out for a walk, the number of hours is &hours")
        public void catNotTakenForAWalk(Integer hours) {
            cat.notTakenForAWalk(hours);
        }

   @When("the Cat did kaki of size &size")
   public void Kaki(int size){
            cat.Kaki(size);
   }

   @Then("the kaki size is &size")
    public void size(int size) {
       try {
           Assert.assertEquals(size, cat.GetKaki());
       }
       catch (Throwable e) {
           throw new ComparisonFailure(null, ((Integer)size).toString(),
                   ((Integer)cat.GetKaki()).toString());
       }

   }
@When("backup")
public void backup() {
}


   @Then("the house condition is &condition")
   private void theHouseCondition(String condition) {
            Assert.assertEquals(condition, cat.houseCondition());
        }




}
