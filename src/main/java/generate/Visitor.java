package generate;

import com.squareup.javapoet.*;
import CustomInput.ClassMethodInput;
import CustomInput.NoInputWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class Visitor extends Pattern {

    final static Logger log = LoggerFactory.getLogger(Visitor.class);

    //    creates concrete methods that would call visit function
    public MethodSpec genConcreteAccept(ClassName type){
        MethodSpec method = MethodSpec.methodBuilder("accept")
                .addModifiers(Modifier.PUBLIC)
                .addParameter(type, "visitor")
                .returns(void.class)
                .addAnnotation(Override.class)
                .addStatement("$N.visit(this)", "visitor")
                .build();

        return method;
    }

    @Override
    public void generateCode(String configPackage, String configPath){

//        prompts user for additional information
        String designName = promptPatternName("Visitor");
        String interName = promptInterface();

        ClassMethodNames cmn = promptClassMethodsName(interName);

        String[] classNames= cmn.classes;
        String[] methodNames = cmn.methods;

        log.info("Received additional information for Visitor");

//        stores methods into array list
        ArrayList<MethodSpec> absMethod = listAbsMethods(methodNames, null, null, null);
        ArrayList<MethodSpec> methods = listMethods(methodNames, true, null, null);

        log.debug("Constructed specified class methods");

        ArrayList<MethodSpec> visitAbsMethod = new ArrayList<MethodSpec>();
        ArrayList<MethodSpec> visitConcreteMethod = new ArrayList<MethodSpec>();

//        gets type of Visitor and interface
        ClassName type = ClassName.get(configPackage, "Visitor");
        ClassName inter = ClassName.get(configPackage, interName);

//        generates the accept method
        absMethod.add(absGenMethod("accept", null, type, "visitor"));
        methods.add(genConcreteAccept(type));

        log.debug("Constructed \"accept\" method");

//        generates interface
        genInterface(interName, absMethod, configPackage, configPath);

        log.info("Generated specified interface");

//        generates classes specified by user
        for(String name : classNames) {
            genClass(name, methods, inter, false, configPackage, configPath);
            ClassName classType = ClassName.get(configPackage, name);
            visitAbsMethod.add(absGenMethod("visit", null, classType, name.toLowerCase()));
            visitConcreteMethod.add(genEmptyMethod("visit", true, classType, name.toLowerCase()));
        }

        log.debug("Constructed Visitor methods");
        log.info("Generated specified classes");

//        generates visitor interface and class
        genInterface("Visitor", visitAbsMethod, configPackage, configPath);
        genClass(designName, visitConcreteMethod, type, false, configPackage, configPath);

        log.info("Generated Visitor interface and class");
    }
}
