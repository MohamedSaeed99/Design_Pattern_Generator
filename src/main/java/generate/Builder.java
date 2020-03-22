package generate;
import com.squareup.javapoet.*;
import CustomInput.ClassMethodInput;
import CustomInput.NoInputWarning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.Arrays;
import java.util.ArrayList;

public class Builder extends Pattern {

    final static Logger log = LoggerFactory.getLogger(Builder.class);

    //    Generates a method to call add method of ComplexObjects object
    public MethodSpec genAddMethod(ClassName paramType){
        MethodSpec add = MethodSpec.methodBuilder("add")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(paramType, "child")
                .addStatement("$N.add($N)", "list", "child")
                .build();
        return add;
    }

    //    Generates ComplexObjects constructor that would instantiate an arrayList
//    of the interface type
    public MethodSpec genComplexConst(ClassName arrayList, ClassName prod){
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$N = new $T<$T>()", "list", arrayList, prod)
                .build();
        return constructor;
    }

    //    Creates ComplexObjects class that would have add method to add passed in objects
    public void genComplexClass(TypeName type, ArrayList<MethodSpec> methods, String pack, String path){
        TypeSpec.Builder complexClass = TypeSpec.classBuilder("ComplexObjects")
                .addModifiers(Modifier.PUBLIC)
                .addField(type, "list", Modifier.PRIVATE);
        for(MethodSpec m : methods){
            complexClass.addMethod(m);
        }
        TypeSpec comClass = complexClass.build();
        storeFile(comClass, "ComplexObjects", pack, path);
    }

    //    Creates methods that would add objects into ComplexObject list
    public MethodSpec genBuilderMethods(String name, ClassName type){
        MethodSpec method = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addStatement("$N.add(new $T())", "co", type)
                .build();
        return method;
    }

    //    Generates method that would return the reference to ComplexObjects
    public MethodSpec genConcreteCompMethod(String name, ClassName type){
        MethodSpec method = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(type)
                .addStatement("return $N", "co")
                .build();
        return method;
    }

    //    generates the Builder constructor that would instantiate type ComplexObjects
//    and stores it into private variable co
    public MethodSpec genBuilderConstructor(ClassName co){
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addStatement("$N = new $T()", "co", co)
                .build();
        return constructor;
    }

    //    Generates Builder class with methods mentioned and private variable co of type ComplexObjects
    public void genBuilderClass(String name, ArrayList<MethodSpec> methods, ClassName fieldVar,
                                ClassName inter, String pack, String path){
        TypeSpec.Builder buildClass = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .addField(fieldVar, "co", Modifier.PRIVATE)
                .addSuperinterface(inter);
        for(MethodSpec m : methods){
            buildClass.addMethod(m);
        }
        TypeSpec builder = buildClass.build();

        storeFile(builder, name, pack, path);
    }

    @Override
    public void generateCode(String configPackage, String configPath){
//        Asks user for additional information about Builder design pattern
        String designName = promptPatternName("Builder");;
        String interName = promptInterface();

        ClassMethodNames cmn = promptClassMethodsName(interName);

        String[] classNames= cmn.classes;
        String[] methodNames = cmn.methods;

        log.info("Received additional information for Builder");

//        gets the types of interface, list, and arrayList
        ClassName inter = ClassName.get(configPackage, interName);
        ClassName list = ClassName.get("java.util", "List");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        TypeName listInter = ParameterizedTypeName.get(list, inter);

        log.debug("Created variables to store types");

//        adds the 2 methods belonging to ComplexObjects: constructor and add method
        ArrayList<MethodSpec> complexMethods = new ArrayList<MethodSpec>();
        complexMethods.add(genComplexConst(arrayList, inter));
        complexMethods.add(genAddMethod(inter));

        log.debug("Constructed ComplexObjects methods");

//        Generates the ComplexObjects class
        genComplexClass(listInter, complexMethods, configPackage, configPath);

        log.info("Generated ComplexObjects class");

//        Data structure to store builder methods and other class methods
        ArrayList<MethodSpec> methods = listMethods(methodNames, true, null, null);
        ArrayList<MethodSpec> absMethods = listAbsMethods(methodNames, null, null, null);
        ArrayList<MethodSpec> builderAbsMethods = new ArrayList<MethodSpec>();
        ArrayList<MethodSpec> builderMethods = new ArrayList<MethodSpec>();

        log.debug("Generated specified methods");

//        generates interface specified by user
        genInterface(interName, absMethods, configPackage, configPath);

        log.info("Generated specified interface");

//        Creates the classes user specified
        for(String name : classNames){
            genClass(name, methods, inter, false, configPackage, configPath);
            ClassName type = ClassName.get(configPackage, name);
            builderAbsMethods.add(absGenMethod("build"+name, null, null, null));
            builderMethods.add(genBuilderMethods("build"+name, type));
        }

        log.info("Generated specified classes");

//        Adds constructor and result functions for Builder and generates builder interface and concrete class
        ClassName complexType = ClassName.get(configPackage, "ComplexObjects");
        builderAbsMethods.add(absGenMethod("result", complexType, null, null));
        builderMethods.add(genConcreteCompMethod("result", complexType));
        builderMethods.add(genBuilderConstructor(complexType));

        log.debug("Generated Builder methods");

        genInterface("Builder", builderAbsMethods, configPackage, configPath);

        genBuilderClass(designName, builderMethods, complexType, ClassName.get(configPackage, "Builder"),
                configPackage, configPath);

        log.info("Generated Builder interface and class");
    }
}