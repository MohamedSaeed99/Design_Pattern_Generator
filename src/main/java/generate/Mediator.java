package generate;

import com.squareup.javapoet.*;
import CustomInput.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class Mediator extends Pattern {

    final static Logger log = LoggerFactory.getLogger(Mediator.class);

    //    Creates methods that would register an instance of the other classes provided
    public MethodSpec genRegister(String name, ClassName paramType, String paramName){
        MethodSpec method = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(paramType, paramName)
                .returns(void.class)
                .addStatement("this.$N = $N", paramName, paramName)
                .build();
        return method;
    }

    //    Generates concrete mediator class
    public void genMediatorClass(String name, ArrayList<MethodSpec> methods, ClassName inter,
                                 ArrayList<ClassName> fieldType, String[] fieldName, String pack, String path){
        TypeSpec.Builder generatedClass = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(inter);
        for(int i = 0; i < fieldName.length; i++){
            generatedClass.addField(fieldType.get(i), fieldName[i].toLowerCase(), Modifier.PRIVATE);
        }
        for(MethodSpec m : methods){
            generatedClass.addMethod(m);
        }
        TypeSpec genClass = generatedClass.build();
        storeFile(genClass, name, pack, path);
    }

    //    Generates constructor of classes to store instances of mediator
    public MethodSpec classConstructor(ClassName medType, String medName){
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(medType, medName.toLowerCase())
                .addStatement("this.$N = $N", medName.toLowerCase(), medName.toLowerCase())
                .build();

        log.debug("Class Constructor Built");
        return constructor;
    }

    //    Generates classes specified by user with private field variable of type Mediator
    public void genClasses(String name, ArrayList<MethodSpec> methods, ClassName inter, ClassName medType,
                           String medName, String pack, String path){
        TypeSpec.Builder classes = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(inter)
                .addField(medType, medName.toLowerCase(), Modifier.PRIVATE);
        for(MethodSpec m : methods){
            classes.addMethod(m);
        }
        TypeSpec classGenerated = classes.build();

        log.debug("Storing class into file");

        storeFile(classGenerated, name, pack, path);
    }

    @Override
    public void generateCode(String configPackage, String configPath) {

//        prompts user for additional information
        String designName = promptPatternName("Mediator");
        String[] designMethods = promptPatternMethods("Mediator");

        String interName = promptInterface();

        ClassMethodNames cmn = promptClassMethodsName(interName);

        String[] classNames= cmn.classes;
        String[] methodNames = cmn.methods;

        log.info("Received all information from user to create Mediator");

//        gets a list of constructed methods
        ArrayList<MethodSpec> desAbsMethods = listAbsMethods(designMethods, null, null, null);
        ArrayList<MethodSpec> desMethods = listMethods(designMethods, true, null, null);
        ArrayList<MethodSpec> absMethods = listAbsMethods(methodNames, null, null, null);
        ArrayList<MethodSpec> methods = listMethods(methodNames, true, null, null);

        log.debug("Created user specified methods");

//        gets the types of objects
        ArrayList<ClassName> classType = new ArrayList<ClassName>();
        ClassName medType = ClassName.get(configPackage, "Mediator");
        ClassName inter = ClassName.get(configPackage, interName);

        methods.add(classConstructor(medType, designName));

        log.debug("Added constructor for classes");

//        generates interface specified by user
        genInterface(interName, absMethods, configPackage, configPath);

        log.info("Generated specified Interface");

//        generates all the classes specified by user
        for(String name: classNames){
            genClasses(name, methods, inter, medType, designName, configPackage, configPath);
            ClassName type = ClassName.get(configPackage, name);
            classType.add(type);
            desAbsMethods.add(absGenMethod("register"+name, null, type, name.toLowerCase()));
            desMethods.add(genRegister("register"+name, type, name.toLowerCase()));
        }

        log.info("Generated all specified Classes");

//        generates mediator interface
        genInterface("Mediator", desAbsMethods, configPackage, configPath);

//        generates mediator class
        genMediatorClass(designName, desMethods, medType, classType, classNames, configPackage, configPath);

        log.info("Generated Mediator interface and concrete class");
    }
}
