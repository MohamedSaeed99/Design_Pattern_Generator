package generate;
import com.squareup.javapoet.*;
import CustomInput.ClassMethodInput;
import CustomInput.NoInputWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class FactoryMethod extends Pattern {

    final static Logger log = LoggerFactory.getLogger(FactoryMethod.class);

    //    Generates the factory class
    public void genFactoryClass(String name, MethodSpec factoryMethod, String pack, String path){
        TypeSpec factClass = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(factoryMethod)
                .build();

        storeFile(factClass, pack, path);
    }

    @Override
    public void generateCode(String configPackage, String configPath){

//        Prompts user for specific inputs
        String designName = promptPatternName("Factory Method");
        String interName = promptInterface();

        ClassMethodNames cmn = promptClassMethodsName(interName);

        String[] classNames= cmn.classes;
        String[] methodNames = cmn.methods;

        log.info("Received additional information for Factory Method");

//        Generates the methods for the classes and interface
        ArrayList<MethodSpec> methods = listMethods(methodNames, true, null, null);
        ArrayList<MethodSpec> absMethods = listAbsMethods(methodNames, null, null, null);

        log.debug("Generated specified class methods");

//        generates interface specified
        genInterface(interName, absMethods, configPackage, configPath);

        log.info("Generated specified interface");

        ArrayList<ClassName> classTypes = new ArrayList<ClassName>();
        ClassName interFile = ClassName.get(configPackage, interName);

//        generates classes specified
        for(String name : classNames){
            genClass(name, methods, interFile, false, configPackage, configPath);
            classTypes.add(ClassName.get(configPackage, name));
        }

        log.info("Generated specified classes");

        MethodSpec factMethod = genFactoryMethod("factoryMethod", interFile, classNames, classTypes);

//        generates factory method class
        genFactoryClass(designName, factMethod, configPackage, configPath);

        log.info("Generated Factory Method class");
    }
}
