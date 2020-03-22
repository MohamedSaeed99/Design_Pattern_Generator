package generate;

import com.squareup.javapoet.*;
import CustomInput.ClassMethodInput;
import CustomInput.NoInputWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class ChainOfResponsibility extends Pattern {
    final static Logger log = LoggerFactory.getLogger(ChainOfResponsibility.class);
    //    generates abstract class of Chain of Responsibility with private field successor
    @Override
    public void genInterface(String name, ArrayList<MethodSpec> methods, String pack, String path){
        TypeSpec.Builder chain = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addField(ClassName.get(pack, name), "successor", Modifier.PRIVATE);
        for(MethodSpec m : methods){
            chain.addMethod(m);
        }
        TypeSpec chainInter = chain.build();

        storeFile(chainInter, name, pack, path);
    }

    //  Generates constructor to store the successor
    public MethodSpec genAbsConstructor(String name, String pack){
        MethodSpec.Builder method = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        if(name != null){
            method.addParameter(ClassName.get(pack, name), "successor")
                    .addStatement("this.$N = $N", "successor", "successor");
            log.debug("Added parameter and statement");
        }
        MethodSpec constructor = method.build();
        return constructor;
    }

    //    Generates handleRequest method with simple implementation
    public MethodSpec genHandleReq(){
        MethodSpec handReq = MethodSpec.methodBuilder("handleRequest")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .beginControlFlow("if ($N != null)", "successor")
                .addStatement("$N.$N()", "successor", "handleRequest")
                .endControlFlow()
                .build();
        return handReq;
    }

    //    Generates constructors for the classes that would call parents constructor(super)
    public MethodSpec classConstructor(ClassName type){
        MethodSpec method = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(type, "successor")
                .addStatement("super($N)", "successor")
                .build();
        return method;
    }

    @Override
    public void generateCode(String configPackage, String configPath){

//        Asks additional information to user
        String interName = promptInterface();

        ClassMethodNames cmn = promptClassMethodsName(interName);

        String[] classNames= cmn.classes;
        String[] methodNames = cmn.methods;

        log.info("Received additional information for Chain of Responsibility");

//        stores methods for classes
        ArrayList<MethodSpec> methods = new ArrayList<MethodSpec>();
        ArrayList<MethodSpec> overMethods = new ArrayList<MethodSpec>();

        methods.add(genAbsConstructor(null, configPackage));
        methods.add(genAbsConstructor(interName, configPackage));
        methods.add(genHandleReq());

        log.debug("Generated constructors and handle request methods");

        overMethods.add(classConstructor(ClassName.get(configPackage, interName)));
        overMethods.add(genEmptyMethod("handleRequest", true, null, null));

//        gets additional user specified methods
        for(String m : methodNames){
            methods.add(absGenMethod(m, null, null, null));
            overMethods.add(genEmptyMethod(m, true, null, null));
        }

        log.debug("Generated user specified methods");

        genInterface(interName, methods, configPackage, configPath);

        log.info("Generated specified interface");

//        creates the different classes specified by user
        for(String name : classNames){
            genClass(name, overMethods, ClassName.get(configPackage, interName), true, configPackage,
                    configPath);
        }

        log.info("Generated specified classes");
    }
}
