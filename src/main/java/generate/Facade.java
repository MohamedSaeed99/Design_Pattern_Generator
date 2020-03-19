package generate;

import com.squareup.javapoet.*;
import CustomInput.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.Arrays;

public class Facade extends Pattern {
    final static Logger log = LoggerFactory.getLogger(Facade.class);
    //    Generates the Facade Class
    public void genClass(String name, ArrayList<MethodSpec> methods, ArrayList<ClassName> type,
                         String[] typeName, String pack, String path){
//        Generates a TypeSpec which consists of class material
        TypeSpec.Builder generatedClass = TypeSpec.classBuilder(name)
                .addModifiers(Modifier.PUBLIC);

//        adds private field variables to class
        if(typeName != null) {
            for (int i = 0; i < typeName.length; i++) {
                generatedClass.addField(type.get(i), typeName[i].toLowerCase(), Modifier.PRIVATE);
                log.debug("Private field {} added", typeName[i]);
            }
        }

//        adds methods to the class
        for(MethodSpec m : methods){
            generatedClass.addMethod(m);
        }
        TypeSpec facadeClass = generatedClass.build();

//        Stores class in file
        storeFile(facadeClass, pack, path);
    }

    //    Generates the Constructor for the Facade class
    public MethodSpec genConstructor(ArrayList<ClassName> type, String[] typeNames){
        MethodSpec.Builder construct = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);
        for(int i = 0; i < typeNames.length; i++){
            construct.addParameter(type.get(i), typeNames[i].toLowerCase()).addStatement("this.$N = $N",
                    typeNames[i].toLowerCase(), typeNames[i].toLowerCase());
        }
        MethodSpec constructor = construct.build();
        log.debug("Constructor generated");
        return constructor;
    }

    @Override
    public void generateCode(String configPackage, String configPath){

//        prompts user for additional information
        String designName = promptPatternName("Facade");
        String[] desMethods = promptPatternMethods("Facade");

        ClassMethodNames cmn = promptClassMethodsName("");

        String[] classNames= cmn.classes;
        String[] methodNames = cmn.methods;

        log.info("Received additional information for Facade");

//        stores methods into arrayList
        ArrayList<MethodSpec> methods = listMethods(methodNames, false, null, null);
        ArrayList<ClassName> classTypes = new ArrayList<ClassName>();

//        generates the classes
        for(String name : classNames){
            genClass(name, methods, null, null, configPackage, configPath);
            classTypes.add(ClassName.get(configPackage, name));
        }

        log.info("Generated specified classes");

        ArrayList<MethodSpec> designMethod = new ArrayList<MethodSpec>();
        designMethod.add(genConstructor(classTypes, classNames));

//        creates methods specific to design pattern
        for(String m: desMethods){
            designMethod.add(genEmptyMethod(m, false, null, null));
        }

        log.debug("Generated Facade methods");

//        generates design pattern class
        genClass(designName, designMethod, classTypes, classNames, configPackage, configPath);

        log.info("Generated Facade class");
    }
}