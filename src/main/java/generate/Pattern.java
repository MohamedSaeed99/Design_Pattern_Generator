package generate;

import CustomInput.*;
import com.squareup.javapoet.*;
import javax.lang.model.element.Modifier;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

import jdk.internal.net.http.common.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Uses composite for all the implementation of the design patterns
public abstract class Pattern {

//    Data Structure to storing class names and method names
    class ClassMethodNames {
        public String[] classes = {};
        public String[] methods = {};
    }

    public abstract void generateCode(String configPackage, String configPath);

    protected Logger log = LoggerFactory.getLogger(Pattern.class);

    //    Retrieves list of design pattern methods from popup
    public String[] promptPatternMethods(String str){
//        loops until valid method names are entered
        String[] methodNames = {};
        do {
            CustomDesignMethodInput mi = new CustomDesignMethodInput(true, str);
            if (mi.showAndGet()) {
                methodNames = mi.getMethod().split(" ");
            }
            if(methodNames.length == 0 || Arrays.asList(methodNames).contains("")){
                NoInputWarning ni = new NoInputWarning(true);
                ni.show();
            }
        }while(methodNames.length == 0 || Arrays.asList(methodNames).contains(""));

        log.info("Pattern methods received: {}", Arrays.toString(methodNames));
        return methodNames;
    }

    //    Retrieves an interface from popup
    public String promptInterface(){

//        loops until valid interface name is given
        String interName = "";
        do {
            CustomInterfaceInput ci = new CustomInterfaceInput(true);
            if (ci.showAndGet()) {
                interName = ci.getInterface();
            }
            if (interName.equals("")) {
                NoInputWarning ni = new NoInputWarning(true);
                ni.show();
            }
        }while(interName.equals(""));

        log.info("Interface received: {}", interName);

        return interName;
    }

    //    Retrieves list of interfaces from popup
    public String[] promptMultiInterface(){
//        loops through until input for interfaces is valid
        String[] interName = {};
        do {
            CustomInterfaceInput ci = new CustomInterfaceInput(true);
            if (ci.showAndGet()) {
                interName = ci.getInterface().split(" ");
            }
            if(interName.length == 0 || Arrays.asList(interName).contains("")){
                NoInputWarning ni = new NoInputWarning(true);
                ni.show();
            }
        }while(interName.length == 0 || Arrays.asList(interName).contains(""));

        log.info("Interfaces received: {}", Arrays.toString(interName));

        return interName;
    }

    //    Retrieves a name for the design pattern class
    public String promptPatternName(String str){
        String designName = "";
        do {
            CustomDesignNameInput di = new CustomDesignNameInput(true, str);
            if (di.showAndGet()) {
                designName = di.getDesign();
            }
            if (designName.equals("")) {
                NoInputWarning ni = new NoInputWarning(true);
                ni.show();
            }
        }while(designName.equals(""));

        log.info("Design pattern class name received: {}", designName);

        return designName;
    }

    public ClassMethodNames promptClassMethodsName(String inter){
        ClassMethodNames cmn = new ClassMethodNames();

//        ensures that the inputs are not empty
        do{
            ClassMethodInput cm = new ClassMethodInput(true, inter);
            if (cm.showAndGet()) {
                cmn.classes = cm.getClasses().split(" ");
                cmn.methods = cm.getMethods().split(" ");
            }
            if(cmn.classes.length == 0 || Arrays.asList(cmn.classes).contains("")
                    || cmn.methods.length == 0 || Arrays.asList(cmn.methods).contains("")){
                NoInputWarning ni = new NoInputWarning(true);
                ni.show();
            }
        } while(cmn.classes.length == 0 || Arrays.asList(cmn.classes).contains("")
                || cmn.methods.length == 0 || Arrays.asList(cmn.methods).contains(""));

        return cmn;
    }


    //    generates a non abstract method
    public MethodSpec genEmptyMethod(String name, boolean override, ClassName paramType, String paramName) {
        MethodSpec.Builder generateMethod = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class);
//        checks if method overrides an abstract method
        if(override == true) {
            generateMethod.addAnnotation(Override.class);
        }
        if(paramType != null){
            generateMethod.addParameter(paramType, paramName);
        }
        MethodSpec method = generateMethod.build();
        return method;
    }

    //    generates an abstract method
    public MethodSpec absGenMethod(String name, ClassName returnType, ClassName paramType, String paramName){
        MethodSpec.Builder generateAbsMethod = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);
        if(returnType != null) {
            generateAbsMethod.returns(returnType);
        }else{
            generateAbsMethod.returns(void.class);
        }
        if(paramType != null){
            generateAbsMethod.addParameter(paramType, paramName);
        }
        MethodSpec abstractMethod = generateAbsMethod.build();
        return abstractMethod;
    }

    //    Generates a simple class and saves it in file
    public void genClass (String name, ArrayList<MethodSpec> method, ClassName inter, boolean abst, String pack, String path){
//       Generates a TypeSpec which consists of class material
        TypeSpec.Builder generatedClass = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);
        for(MethodSpec m : method){
            generatedClass.addMethod(m);
        }
        if(abst == true){
            generatedClass.superclass(inter);
        }
        else {
            generatedClass.addSuperinterface(inter);
        }
        TypeSpec genClass = generatedClass.build();
//        Creates a class file in output directory
        storeFile(genClass, pack, path);
    }

    //    Generates basic and empty client class
    public void genClient(String pack, String path){
//        Creates the "main" method of the class which will be implemented by user
        MethodSpec mainMethod = MethodSpec.methodBuilder("main")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(void.class)
                .addParameter(String[].class, "args")
                .build();

//        Creates the client class which will invoke the design patterns by the user
        TypeSpec clientClass = TypeSpec.classBuilder("Client")
                .addModifiers(Modifier.PUBLIC)
                .addMethod(mainMethod)
                .build();

        storeFile(clientClass, pack, path);
    }

    //    returns a list of abstract methods
    public ArrayList<MethodSpec> listAbsMethods(String[] methods, ClassName returnType, ClassName paramType, String paramName){
        ArrayList<MethodSpec> m = new ArrayList<MethodSpec>();
        for(String desM : methods){
            m.add(absGenMethod(desM, returnType, paramType, paramName));
        }
        return m;
    }

    //    returns a list of empty methods
    public ArrayList<MethodSpec> listMethods(String[] methods, boolean override, ClassName paramType, String paramName){
        ArrayList<MethodSpec> m = new ArrayList<MethodSpec>();
        for(String desM : methods){
            m.add(genEmptyMethod(desM, override, paramType, paramName));
        }
        return m;
    }

    //    generates an interface/abstract class
    public void genInterface(String name, ArrayList<MethodSpec> methods, String pack, String path){
        TypeSpec.Builder inter = TypeSpec.interfaceBuilder(name);
        for(MethodSpec m : methods){
            inter.addMethod(m);
        }
        TypeSpec interfaceGen = inter.build();

        storeFile(interfaceGen, pack, path);
    }

    //    Generates the Factory method
    public MethodSpec genFactoryMethod(String name, ClassName interType, String[] classNames, ArrayList<ClassName> classTypes){
        MethodSpec.Builder method = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(interType)
                .addParameter(String.class, "prod")

//                Constructs the first if and starts the control flow
                .beginControlFlow("if ($N.equals($S))", "prod", classNames[0])
                .addStatement("return new $T()", classTypes.get(0));

//        constructs the else if statements
        for (int i = 1; i < classNames.length; i++){
            method.nextControlFlow("else if ($N.equals($S))", "prod", classNames[i])
                    .addStatement("return new $T()", classTypes.get(i));
        }

//        constructs the else statement which returns null
        method.nextControlFlow("else")
                .addStatement("return null");

//        Ends the control flow and builds the method
        MethodSpec factoryMethod = method.endControlFlow().build();
        return factoryMethod;
    }

    //    Stores the class into a file
    public void storeFile(TypeSpec classBuild, String pack, String path){
        //        Creates a class file in output directory
        JavaFile javaFile = JavaFile.builder(pack, classBuild)
                .addFileComment("AUTO_GENERATED BY JavaPoet")
                .build();

        try {
            javaFile.writeTo(Paths.get(path));//root maven source

        } catch (IOException ex) {
            log.error("Couldn't write file error: {}", ex.getMessage());
        }
    }
}
