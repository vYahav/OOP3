package tests;

import Provided.StoryTestException;
import Provided.StoryTester;
import Solution.StoryTesterImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CatTest {


    /**
     * This is a minimal test. Write your own tests!
     * Please don't submit your tests!
     */
    

        private StoryTester tester;
        private String goodStory;
        private String badStory;
        private String derivedStory;
        private String nestedStory;
        private Class<?> testClass;
        private Class<?> derivedTestClass;

        @Before
        public void setUp() throws Exception {
            goodStory = "Given a cat of age 6\n"
                    + "When the cat is not taken out for a walk, the number of hours is 5\n"
                    + "Then the house condition is clean";

            badStory = "Given a Cat of age 6\n"
                    + "When the cat is not taken out for a walk, the number of hours is 5\n"
                    + "Then the house condition is smelly";

            derivedStory = "Given a Cat of age 6\n"
                    + "When the cat is not taken out for a walk, the number of hours is 15\n"
                    + "When the house is cleaned, the number of hours is 11\n"
                    + "Then the house condition is clean";

            nestedStory = "Given a Cat that his age is 6\n"
                    + "When the cat is not taken out for a walk, the number of hours is 5\n"
                    + "Then the house condition is clean";
            testClass = CatStory.class;
            //derivedTestClass = DogStoryDerivedTest.class;
            tester = new StoryTesterImpl();
        }


        @Test
        public void test1() throws Exception {
            try {
                tester.testOnInheritanceTree(goodStory, testClass);
                Assert.assertTrue(true);
            } catch (StoryTestException e) {
                Assert.assertTrue(false);
            }





            goodStory = "Given a cat of age 6 and name Laki\n"
                    + "When the cat is not taken out for a walk, the number of hours is 5\n"
                    + "Then the house condition is smelly";
            try {
                tester.testOnInheritanceTree(goodStory, testClass);
                Assert.assertTrue(false);
            } catch (StoryTestException e) {
                Assert.assertTrue(true);
            }


            goodStory = "Given a cat of age 6 and name Laki\n"
                    + "When the cat is not taken out for a walk, the number of hours is 5\n"
                    + "Then the house condition is clean";
            try {
                tester.testOnInheritanceTree(goodStory, testClass);
                Assert.assertTrue(true);
            } catch (StoryTestException e) {
                Assert.assertTrue(false);
            }


        }
    }

