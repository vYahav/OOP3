package Solution;

import Provided.StoryTester;

import java.lang.annotation.*;
import java.lang.reflect.Method;

enum testEnum{
    test1,test2
}


public class StoryTesterImpl implements StoryTester {
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Given{
        String value();
    }
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface When{
        String value();
    }
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Then{
        String value();
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




    @Override
    public void testOnInheritanceTree(String story, Class<?> testClass) throws Exception {
        //TODO: Create object of testClass
        String[] sentences = story.split("\n"); // splits by new line

        for (String sentence : sentences) {
            String[] words= sentence.split("\\s+"); // splits by whitespace
            String content="";
            for(int i=1;i<words.length;i++){
               content.concat(words[i]);
            }

            Annotation myAnnotation=getAnnotation(words[0],content);
            Class annotClass=myAnnotation.getClass();

            for(Method method : testClass.getDeclaredMethods()){
                if (method.isAnnotationPresent(annotClass)) {
                    //TODO: Call testClass method with myAnnotation
                }
            }

        }
    }

    @Override
    public void testOnNestedClasses(String story, Class<?> testClass) throws Exception {
        //TODO: Implement
    }
}
