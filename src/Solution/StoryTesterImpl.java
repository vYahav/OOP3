package Solution;

import Provided.*;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;


public class StoryTesterImpl implements StoryTester {


    Class<? extends Annotation> annotationMaker(String annot){
        if(annot.equals("Given")) {
            return Given.class;
        }

        if(annot.equals("When")) {
            return When.class;
        }

        if(annot.equals("Then")) {
            return Then.class;
        }
        return null;
    }
    Given getInstanceOfAnnotationGiven(final String val)
    {
        Given annotation = new Given()
        {
            public String value()
            {
                return val;
            }

            @Override
            public Class<? extends Annotation> annotationType()
            {
                return Given.class;
            }
        };

        return annotation;
    }
    When getInstanceOfAnnotationWhen(final String val)
    {
        When annotation = new When()
        {
            public String value()
            {
                return val;
            }

            @Override
            public Class<? extends Annotation> annotationType()
            {
                return When.class;
            }
        };

        return annotation;
    }
    Then getInstanceOfAnnotationThen(final String val)
    {
        Then annotation = new Then()
        {
            public String value()
            {
                return val;
            }

            @Override
            public Class<? extends Annotation> annotationType()
            {
                return Then.class;
            }
        };

        return annotation;
    }
    Annotation getAnnotation(String annot,String val){
        if(annot.equals("Given")) {
            return getInstanceOfAnnotationGiven(val);
        }

        if(annot.equals("When")) {
            return getInstanceOfAnnotationWhen(val);
        }

        if(annot.equals("Then")) {
            return getInstanceOfAnnotationThen(val);
        }
        return null;
    }


     ArrayList<ArrayList<String>> StoryBreakdown(String story){
        int index = 0;
        String[] singleSentence = story.split("\n");

        ArrayList<ArrayList<String>> mySentences = new ArrayList<>();
        ArrayList<String> tmpSentence;
        for(String currFullSentence: singleSentence){
            String[] currSentenceWords = currFullSentence.split(" ");
            ArrayList<String> currSentenceList = new ArrayList<>();
            mySentences.add(index, currSentenceList);
            tmpSentence = mySentences.get(index);
            for(String word: currSentenceWords){
                tmpSentence.add(word);
            }
            index++;
        }
        return mySentences;
    }




    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {
        //TODO: Create object of testClass


    }

    @Override
    public void testOnNestedClasses(String story, Class<?> testClass) throws Exception {
        //TODO: Implement
    }
}
