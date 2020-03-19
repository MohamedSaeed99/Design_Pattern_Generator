package generate;

import java.util.ArrayList;
import java.util.Arrays;
import com.squareup.javapoet.*;
import CustomInput.ClassMethodInput;
import CustomInput.NoInputWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;

public class TemplateMethod extends Pattern {

    final static Logger log = LoggerFactory.getLogger(TemplateMethod.class);

    //    Generates the template method that calls the other functions
    public MethodSpec genTemplateMethod(String[] methods){
        MethodSpec.Builder tempMethod = MethodSpec.methodBuilder("templateMethod")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .returns(void.class);
        for(String m : methods){
            tempMethod.addStatement("$N()", m);
        }
        MethodSpec templateMethod = tempMethod.build();
        return templateMethod;
    }

    //    generates abstract class with template method
    public void genAbstractClass(String name, ArrayList<MethodSpec> methods, String pack, String path){
        TypeSpec.Builder tempClass = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        for(MethodSpec m : methods){
            tempClass.addMethod(m);
        }
        TypeSpec genTempClass = tempClass.build();
        storeFile(genTempClass, pack, path);
    }

    @Override
    public void generateCode(String configPackage, String configPath) {
//        prompts user for additional information
        String interName = promptInterface();

        ClassMethodNames cmn = promptClassMethodsName(interName);

        String[] classNames= cmn.classes;
        String[] methodNames = cmn.methods;

        log.info("Received additional information for Template Method");

        ArrayList<MethodSpec> methods = listMethods(methodNames, true, null, null);
        ArrayList<MethodSpec> absMethods = listAbsMethods(methodNames, null,null, null);

        log.debug("Constructed specified methods");

//        generates template method
        absMethods.add(genTemplateMethod(methodNames));

        log.debug("Constructed Template Method");

//        generate the abstract class that will hold the temp method
        genAbstractClass(interName, absMethods, configPackage, configPath);

        log.info("Generated abstract class that holds Template Method");

        ClassName inter = ClassName.get(configPackage, interName);

//        generates classes specified by user
        for(String name : classNames){
            genClass(name, methods, inter, true, configPackage, configPath);
        }

        log.info("Generated specified classes");
    }
}
