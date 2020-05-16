package Solution;

import Provided.*;
import junit.framework.ComparisonFailure;

import java.lang.annotation.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


public class StoryTesterImpl implements StoryTester {
    class MethodAndParams{
        Method method;
        ArrayList<ArrayList<String>> paremters;
        MethodAndParams(){
            method = null;
            paremters = null;
        }
    }

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


     ArrayList<ArrayList<String>> storyBreakdown(String story){
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



     Object[] fillWithParameters(ArrayList<String> myList){
        //This func check if every parameter is of type integer or string
        //and inserts the parameter to its place in the array of parameters (Object[] myParameters).
        int typesLen=myList.size();
        Object[] myParameters = new Object[typesLen];
        int index=0;
        for(String s: myList){
            try{ //Integer case
                myParameters[index] = Integer.parseInt(s);
            }catch(NumberFormatException e){ //String case
                myParameters[index] = s;
            }

            index++;
        }

        return myParameters;
    }



    ArrayList< ArrayList<String> > getParametersOfGivenAnnotation(ArrayList<String> sentenceList, Given given){
        ArrayList<String> sentence = new ArrayList<>(sentenceList);//Create a copy of my sentence list
        sentence.remove(0); //Remove "Given" from my sentence

         String[] myWords = given.value().split(" "); //Breakdown annotation's string
         int myWordsLen=myWords.length;
        if(sentence.size() != myWordsLen) return new ArrayList<>(); //If my sentence does not match annotation, return empty list.

        ArrayList<ArrayList<String>> parameters = new ArrayList<>();
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

    ArrayList<ArrayList<String>> getParametersOfWhenAnnotation(ArrayList<String> sentenceList, When when){
        ArrayList<String> sentence = new ArrayList<>(sentenceList); //Create a copy of my sentence list
        sentence.remove(0); //Remove "Given" from my sentence

        String[] myWords = when.value().split(" "); //Breakdown annotation's string
        int myWordsLen=myWords.length;
        if(sentence.size() != myWordsLen) return new ArrayList<>(); //If my sentence does not match annotation, return empty list.

        ArrayList<ArrayList<String>> parameters = new ArrayList<>();
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
    int LenUntilOr(ArrayList<String> sentence){
        int len_until_or = 0;
        for(int i = 0 ; i < sentence.size() ; i++){
            if(sentence.get(i).equals("or"))
                return len_until_or;
            len_until_or++;
        }
    return len_until_or; //the len of the string in case or doesnt exists
    }
    ArrayList<ArrayList<String>> getParametersOfThenAnnotation(ArrayList<String> sentenceList, Then then){
        int wordCounter=0;
        ArrayList<String> sentence = new ArrayList<>(sentenceList);//Create a copy of my sentence list
        sentence.remove(0); //Remove "Given" from my sentence
        String[] myWords = then.value().split(" "); //Breakdown annotation's string
        int myWordsLen=myWords.length;

        int sentence_len = sentence.size();
        if(sentence_len < myWordsLen)
            return new ArrayList<>();
        //Sentence_len can be greater than "then" len because we may have "or"

        if(LenUntilOr(sentence) != myWordsLen) return new ArrayList<>(); //If my sentence does not match annotation, return empty list.


        ArrayList<ArrayList<String>> parameters = new ArrayList<>();
        ArrayList<String> param_inst = new ArrayList<>();
        parameters.add(param_inst);
        int param_list = 0;
        for(int i = 0; i < sentence_len; i++ ){ //For loop through the words in the annotation

            if(wordCounter > myWordsLen - 1 && !(sentence.get(i).equals("or"))) return new ArrayList<>();

            if(sentence.get(i).equals("or")){ //Checks if current word is "or" and act accordingly
                wordCounter = 0;
                ArrayList<String> new_param_inst = new ArrayList<>();
                parameters.add(new_param_inst );
                param_list ++;
                continue;
            }

            if('&' == myWords[wordCounter].charAt(0)){ //Checks if current word in annotation is a value which needs to be provided.
                ArrayList<String> tmp = parameters.get(param_list);
                tmp.add(sentence.get(i));
                continue;
            }

            //Checks if the current word in sentence matches current word in annotation. If not, return empty list
            if(!(myWords[wordCounter].equals(sentence.get(i)))) return new ArrayList<>();
            wordCounter++;

        }
        return parameters;
    }

    Object[] backupSauce(Object myInstance, Field[] myFields) throws IllegalAccessException{
        //Creates a backup (Object array) for the instance of the fields.
        int index = 0, myFieldsLen=myFields.length;
        Method myMethod;
        Object[] myBackup = new Object[myFieldsLen];
        Constructor<?> myConstructor;
        for(Field field : myFields){
            field.setAccessible(true);
            try{ //Tries to clone field and insert it into myBackup
                myMethod = field.getType().getDeclaredMethod("clone");
                myMethod.setAccessible(true);
                myBackup[index] = myMethod.invoke(field.get(myInstance));
            }
            catch(Exception blah){ //Cloning didn't work, using copy constructor to insert field.
                try{
                    myConstructor = field.getType().getDeclaredConstructor(field.getType());
                    myConstructor.setAccessible(true);
                    myBackup[index] = myConstructor.newInstance(field.get(myInstance));
                }
                catch (Exception blahblah){ //Copy constructor didn't work, inserting by refrence
                    myBackup[index] = field.get(myInstance);
                }
            }

            index++;
        }

        return myBackup;
    }

    MethodAndParams getAnnotationsMethod(ArrayList<String> sentence, Class<?> testClass){//If class has a method with same annotation, return its methods with the list of parameters.
         MethodAndParams myMethod = new MethodAndParams();
         ArrayList<ArrayList<String>> tmp;

        if(sentence.get(0).equals("Given")){
            for(Method m: Arrays.stream(testClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Given.class)).collect(Collectors.toList())){
                tmp = getParametersOfGivenAnnotation(sentence, m.getAnnotation(Given.class));
                if(!(tmp.isEmpty())){
                    m.setAccessible(true);
                    myMethod.method = m;
                    myMethod.paremters = tmp;
                    break;
                }
            }
            if(myMethod.method == null) return  null;
            return myMethod;
        }
        if(sentence.get(0).equals("When")){
            for(Method m: Arrays.stream(testClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(When.class)).collect(Collectors.toList())){
                tmp = getParametersOfWhenAnnotation(sentence, m.getAnnotation(When.class));
                if(!(tmp.isEmpty())){
                    m.setAccessible(true);
                    myMethod.method = m;
                    myMethod.paremters = tmp;
                    break;
                }
            }
            if(myMethod.method == null) return  null;
            return myMethod;
        }
         if(sentence.get(0).equals("Then")){
            for(Method m: Arrays.stream(testClass.getDeclaredMethods()).filter(m -> m.isAnnotationPresent(Then.class)).collect(Collectors.toList())){
                tmp = getParametersOfThenAnnotation(sentence, m.getAnnotation(Then.class));
                if(!(tmp.isEmpty())){
                    m.setAccessible(true);
                    myMethod.method = m;
                    myMethod.paremters = tmp;
                    break;
                }
            }
             if(myMethod.method == null) return  null;
             return myMethod;
        }
         return null;
    }

/**
 Param MySentece: Some sentence of the stroy, which starts with the resereved words
 Param TestClass: The given class
 Return the only method and the params(if it is then, there can be more than one) that describes
 the annotaion.
 **/
  MethodAndParams getMethodFromClassTree(ArrayList<String> mySentence, Class<?> testClass){ //Checks in class tree if a method which matches our needs exists
        //if true the annotation was not found.
        if(testClass == null) return null;

        Class superClass=testClass.getSuperclass();
        MethodAndParams goodm;
        goodm = getAnnotationsMethod(mySentence, testClass);
        if(goodm != null) return goodm; //empty check

        return getMethodFromClassTree(mySentence, superClass);
    }






    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {
        if(story == null) throw new IllegalArgumentException();
        if(testClass == null) throw new IllegalArgumentException();

        //Declarations
        ArrayList<ArrayList<String>> storyBreakdown = storyBreakdown(story); //The breakdown of the story to list of sentences, and every sentence holds a list of words
        StoryTestExceptionImpl myException = new StoryTestExceptionImpl(); //Exception to build up later on...
        boolean whenCalled = false; //False until 'When' appears. If 'Then' appears, it resets back to false.
        Object[] myBackup = null; //Object to put the backup in.
        Object testClassInstance = testClass.newInstance();
        String currCommand;//=Given/When/Then
        MethodAndParams goodMethod;
        ArrayList<String> currParameters;
        Method method;


        for(ArrayList<String> currSentenceList : storyBreakdown){ //Iterates through the sentences in the story
             currCommand = currSentenceList.get(0); //Sets current command to be the first word of the sentence (Given/When/Then)
            if(currCommand.equals("Given")){ //'Given' case
                goodMethod = getMethodFromClassTree(currSentenceList, testClass);
                if(null == goodMethod) throw new GivenNotFoundException();

                currParameters = goodMethod.paremters.get(0);
                method = goodMethod.method; //Returns the good, matching method
                method.setAccessible(true);
                method.invoke(testClassInstance, fillWithParameters(currParameters));
            }


            if(currCommand.equals("When")){ //'When' case
                if(!whenCalled){ //First time 'When' appears or first time when appears after a 'Then'.
                    myBackup = backupSauce(testClassInstance,testClass.getDeclaredFields());
                    whenCalled = true;
                }

                goodMethod = getMethodFromClassTree(currSentenceList, testClass);
                if(null == goodMethod) throw new WhenNotFoundException();

                currParameters = goodMethod.paremters.get(0);
                method = goodMethod.method; //Returns the good, matching method
                method.setAccessible(true);
                method.invoke(testClassInstance, fillWithParameters(currParameters));
            }


            if(currCommand.equals("Then")){ //'Then' case
                //Declarations (Special declarations for 'Then')
                boolean finishedSuccessfully = false;
                ArrayList<String> actualOutput=new ArrayList<>(), expectedOutput = new ArrayList<>();
                goodMethod = getMethodFromClassTree(currSentenceList, testClass);

                if(null == goodMethod) throw new ThenNotFoundException(); //No matching methods found...

                //Iterate through all of the sentences and invoke them all.
                for(ArrayList<String> sentence : goodMethod.paremters){ //ArrayList<String> sentence : ArrayList<ArrayList<String>>story

                    method = goodMethod.method; //Returns the good, matching method
                    try{
                        method.setAccessible(true);
                        method.invoke(testClassInstance, fillWithParameters(sentence)); //Invoke method of the current sentence with its parameters.
                        finishedSuccessfully = true;
                        break;
                    }catch(InvocationTargetException e){
                        expectedOutput.add(((org.junit.ComparisonFailure) e.getCause()).getExpected());
                        actualOutput.add(((org.junit.ComparisonFailure) e.getCause()).getActual());
                    }
                }

                if(!finishedSuccessfully){ //FAILURE (didn't finish successfully)
                    myException.incNumFails();
                    if(myException.getNumFail() == 1){ //First time of failing. Building myException:
                        String connectedString = currSentenceList.get(0); //Takes a list of words (sentence) and concats them to a single string (connectedString)
                        for(int i=1; i < currSentenceList.size(); i++){
                            connectedString = connectedString.concat(" " + currSentenceList.get(i));
                        }
                        //Builds the exception
                        myException.setStoryActual(actualOutput);
                        myException.setStoryExpected(expectedOutput);
                        myException.setSentence(connectedString);
                    }

                    //Backup
                    int index = 0;
                    for (Field f : testClass.getDeclaredFields()) {
                        f.setAccessible(true);
                        f.set(testClassInstance,myBackup[index]);
                        index++;
                    }
                }
                whenCalled = false;
            }
        }
        if(myException.getNumFail() > 0){
            throw myException;
        }
    }
}
