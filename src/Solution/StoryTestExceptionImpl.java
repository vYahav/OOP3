package Solution;

import Provided.StoryTestException;

import java.util.ArrayList;
import java.util.List;

public class StoryTestExceptionImpl extends StoryTestException {
     List<String> actualStory;
     List<String> expectedStory;
     String mySentence;
     int numOfFails;



    public StoryTestExceptionImpl(){

        expectedStory = new ArrayList<>();
        numOfFails = 0;
        actualStory = new ArrayList<>();
        mySentence = null;
    }
    @Override
    public String getSentence() {
        return mySentence;
    }
    public StoryTestExceptionImpl setSentence(String s){
        mySentence = new String(s);
        return this;
    }



    @Override
    public List<String> getStoryExpected() {
        return expectedStory;
    }
    public StoryTestExceptionImpl setStoryExpected(ArrayList<String> list){
        expectedStory = new ArrayList<>(list);
        return this;
    }
    @Override
    public List<String> getTestResult() {
        return actualStory;
    }
    public StoryTestExceptionImpl setStoryActual(ArrayList<String> list){
        actualStory = new ArrayList<>(list);
        return this;
    }
    @Override
    public int getNumFail() {
        return numOfFails;
    }
    public StoryTestExceptionImpl incNumFails(){
        numOfFails++;
        return this;
    }

}