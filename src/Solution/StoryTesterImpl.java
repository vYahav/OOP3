package Solution;

import Provided.*;

import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


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

     ArrayList<ArrayList> getParametersOfGivenAnnotation(ArrayList<String> sentenceList, Given given){
        ArrayList<String> sentence = new ArrayList<>(sentenceList);//Create a copy of my sentence list
        sentence.remove(0); //Remove "Given" from my sentence

         String[] myWords = given.value().split(" "); //Breakdown annotation's string
         int myWordsLen=myWords.length;
        if(sentence.size() != myWordsLen) return new ArrayList<>(); //If my sentence does not match annotation, return empty list.

         ArrayList<ArrayList> parameters = new ArrayList<>();
         ArrayList<String> param_inst = new ArrayList<>();
         parameters.add(param_inst);

        for(int i = 0; i < myWordsLen; i++ ){ //For loop through the words in the annotation
            if('&' == myWords[i].charAt(0)){ //Checks if current word in annotation is a value which needs to be provided.
                ArrayList<String> tmp = parameters.get(0);
                tmp.add(sentence.get(i));
                continue;
            }

            //Checks if the current word in sentence matches current word in annotation. If not, return empty list
            if(!(myWords[i].equals(sentence.get(i)))) return new ArrayList<>();
        }
        return parameters;
    }

    ArrayList<ArrayList> getParametersOfWhenAnnotation(ArrayList<String> sentenceList, When when){
        ArrayList<String> sentence = new ArrayList<>(sentenceList); //Create a copy of my sentence list
        sentence.remove(0); //Remove "Given" from my sentence

        String[] myWords = when.value().split(" "); //Breakdown annotation's string
        int myWordsLen=myWords.length;
        if(sentence.size() != myWordsLen) return new ArrayList<>(); //If my sentence does not match annotation, return empty list.

        ArrayList<ArrayList> parameters = new ArrayList<>();
        ArrayList<String> param_inst = new ArrayList<>();
        parameters.add(param_inst);

        for(int i = 0; i < myWordsLen; i++ ){ //For loop through the words in the annotation
            if('&' == myWords[i].charAt(0)){ //Checks if current word in annotation is a value which needs to be provided.
                ArrayList<String> tmp = parameters.get(0);
                tmp.add(sentence.get(i));
                continue;
            }

            //Checks if the current word in sentence matches current word in annotation. If not, return empty list
            if(!(myWords[i].equals(sentence.get(i)))) return new ArrayList<>();
        }
        return parameters;
    }

    ArrayList<ArrayList> getParametersOfThenAnnotation(ArrayList<String> sentenceList, Then then){
        int wordCounter=0;
        ArrayList<String> sentence = new ArrayList<>(sentenceList);//Create a copy of my sentence list
        sentence.remove(0); //Remove "Given" from my sentence

        String[] myWords = then.value().split(" "); //Breakdown annotation's string
        int myWordsLen=myWords.length;
        if(sentence.size() != myWordsLen) return new ArrayList<>(); //If my sentence does not match annotation, return empty list.

        ArrayList<ArrayList> parameters = new ArrayList<>();
        ArrayList<String> param_inst = new ArrayList<>();
        parameters.add(param_inst);



        for(int i = 0; i < myWordsLen; i++ ){ //For loop through the words in the annotation

            if(wordCounter > myWordsLen - 1 && !(sentence.get(i).equals("or"))) return new ArrayList<>();

            if(sentence.get(i).equals("or")){ //Checks if current word is "or" and act accordingly
                wordCounter = 0;
                ArrayList<String> new_param_inst = new ArrayList<>();
                parameters.add(new_param_inst);
                continue;
            }
            wordCounter++;
            if('&' == myWords[i].charAt(0)){ //Checks if current word in annotation is a value which needs to be provided.
                ArrayList<String> tmp = parameters.get(0);
                tmp.add(sentence.get(i));
                continue;
            }

            //Checks if the current word in sentence matches current word in annotation. If not, return empty list
            if(!(myWords[i].equals(sentence.get(i)))) return new ArrayList<>();


        }
        return parameters;
    }



     HashMap<Method,ArrayList<ArrayList>> getAnnotationsMethod(ArrayList<String> sentence, Class<?> testClass){//If class has a method with same annotation, return its methods with the list of parameters.
         HashMap<Method, ArrayList<ArrayList>> myMethod = new HashMap<>();
         ArrayList<ArrayList> tmp;

        if(sentence.get(0).equals("Given")) {
            for( Method m: Arrays.stream(testClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Given.class)).collect(Collectors.toList()) ){
                tmp = getParametersOfGivenAnnotation(sentence, m.getAnnotation(Given.class));
                if(!(tmp.isEmpty())){
                    m.setAccessible(true);
                    myMethod.put(m,tmp);
                    break;
                }
            }
            return myMethod;
        }
        if(sentence.get(0).equals("When")) {
            for( Method m: Arrays.stream(testClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(When.class)).collect(Collectors.toList()) ){
                tmp = getParametersOfWhenAnnotation(sentence, m.getAnnotation(When.class));
                if(!(tmp.isEmpty())){
                    m.setAccessible(true);
                    myMethod.put(m,tmp);
                    break;
                }
            }
            return myMethod;
        }
         if(sentence.get(0).equals("Then")) {
            for( Method m: Arrays.stream(testClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Then.class)).collect(Collectors.toList()) ){
                tmp = getParametersOfThenAnnotation(sentence, m.getAnnotation(Then.class));
                if(!(tmp.isEmpty())){
                    m.setAccessible(true);
                    myMethod.put(m,tmp);
                    break;
                }
            }
            return myMethod;
        }
         return null;
    }


    HashMap<Method,ArrayList<ArrayList>> classTreeCheck(ArrayList<String> mySentence, Class<?> testClass){ //Checks in class tree if a method which matches our needs exists
        Class superClass;
        HashMap<Method, ArrayList<ArrayList>> goodm;

        if(testClass == null) return new HashMap<>(); //null check

        goodm = getAnnotationsMethod(mySentence, testClass);
        if(!(goodm.isEmpty())) return goodm; //empty check

        superClass=testClass.getSuperclass();
        return classTreeCheck(mySentence, superClass);
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
