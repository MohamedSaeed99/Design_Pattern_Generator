package generate;
import com.squareup.javapoet.*;
import CustomInput.ClassMethodInput;
import CustomInput.NoInputWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class AbstractFactory extends Pattern {

    final static Logger log = LoggerFactory.getLogger(AbstractFactory.class);

    //    generates an abstract method
    public MethodSpec absGenMethod(String name, ClassName returnType, String paramName){
        MethodSpec generateAbsMethod = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .returns(returnType)
                .addParameter(String.class, paramName)
                .build();
        return generateAbsMethod;
    }

    //    Generates code by delegating parts of the generation to other functions
    @Override
    public void generateCode(String configPackage, String configPath){
        String designName = promptPatternName("Abstract Factory");
        String[] interName = promptMultiInterface();

        ArrayList<String[]> classes = new ArrayList<String[]>();
        ArrayList<String[]> listMethods = new ArrayList<String[]>();

//        Ensures that the user has a valid input
        for (String s : interName) {

            ClassMethodNames cmn = promptClassMethodsName(s);

            classes.add(cmn.classes);
            listMethods.add(cmn.methods);
        }

        log.info("Received additional user information for Abstract Factory");

//        Stores the methods created into arrayLists
        ArrayList<MethodSpec> abstractFactMethod = new ArrayList<MethodSpec>();
        ArrayList<MethodSpec> concreteMethod = new ArrayList<MethodSpec>();

//        Generates the specified interfaces
        ArrayList<ClassName> interType = new ArrayList<ClassName>();
        for(int i = 0; i < interName.length; i++) {
//            creates the abstract methods and empty methods
            ArrayList<MethodSpec> absMethods = listAbsMethods(listMethods.get(i), null, null, null);
            ArrayList<MethodSpec> methods = listMethods(listMethods.get(i), true, null, null);

//            generates the interface and gets the type of it
            genInterface(interName[i], absMethods, configPackage, configPath);
            interType.add(ClassName.get(configPackage, interName[i]));

//            Generates the classes for each interface and retrieves their types for later use
            ArrayList<ClassName> classTypes = new ArrayList<ClassName>();
            for(String s : classes.get(i)) {
                genClass(s, methods, interType.get(i), false, configPackage, configPath);
                ClassName types = ClassName.get(configPackage, s);
                classTypes.add(types);
            }

            abstractFactMethod.add(absGenMethod("create"+interName[i], interType.get(i), interName[i].toLowerCase()));
            concreteMethod.add(genFactoryMethod("create"+interName[i], interType.get(i), classes.get(i), classTypes));
        }

        log.info("Generated specified interfaces");
        log.debug("Constructed list of Abstract Factory methods");
        log.info("Generated specified classes");

//        Creates interface of abstract factory
        genInterface("AbstractFactory", abstractFactMethod, configPackage, configPath);
        ClassName absFact = ClassName.get(configPackage, "AbstractFactory");

//        Implements the abstract factory
        genClass(designName, concreteMethod, absFact, false, configPackage, configPath);

        log.info("Generated Abstract Factory interface and concrete class");
    }
}