import CustomInput.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import generate.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.squareup.javapoet.*;
import plugin.ClashDetect;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestCode{

    Config config;
    Generator gen;

    @Before
    public void initObjects(){
        config = ConfigFactory.load();
    }


/*
########################################################################################################################
############################################# Homework 3 Test Cases ####################################################
########################################################################################################################
*/

//  gets the initial files in directory
    public void retrieveFiles(ClashDetect cd){
//        Gets the predefined test files from the directory
        String path = config.getString("testInitialDir") + "/" + config.getString("testInitialPack");
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

//        adds initial files to the list
        if(listOfFiles != null){
            for (int i = 0; i < listOfFiles.length; i++) {
                cd.addToMap(listOfFiles[i].getName(), "C:/Users/moesa/Desktop/CS474/mohamed_saeed_hw3/src/test/java/InitialFileList");
            }
        }
    }

//    Removes deleted files
    public void update(ClashDetect cd){
//        Gets the predefined test files from the directory
        String path = config.getString("testInitialDir") + "/" + config.getString("testInitialPack");
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();

        ArrayList<String> files = new ArrayList<String>();
        ArrayList<String> removedFiles = new ArrayList<String>();

//        adds initial files to the list
        if(listOfFiles != null){
            for (int i = 0; i < listOfFiles.length; i++) {

            }
        }

        Map<String, String> fileNames = cd.listOfFiles();

//        checks and updates the filename data structure
        for(Map.Entry<String, String> mp : fileNames.entrySet()){
            if(!files.contains(mp.getKey())){
                removedFiles.add(mp.getKey());
            }
        }

        cd.removeKey(removedFiles);
    }

    @Test
//    This function illustrates the process I did when programing the actual function.
//
//    Process:
//          Have access to the directory
//          Read all the files
//          Convert to string and store them
//
//    Tests the initial retrieval process of files
    public void testInitialSetup(){
        ClashDetect cd = new ClashDetect();
        String[] files = {"A.txt", "B.txt", "C.txt"};

        retrieveFiles(cd);

//        checks if file name is stored in data structure
        for(String file : files){
            Assert.assertTrue(cd.isFound(file, "C:/Users/moesa/Desktop/CS474/mohamed_saeed_hw3/src/test/java/InitialFileList"));
        }

//        clears the data structure
        cd.removeAll();
    }


    @Test
//    This function illustrates the process I did when programing it.
//
//    Process:
//          From given list, delete file
//          Update the list by removing deleted file from data structure
//          Check if the list is accurate
//
//    Tests the update functionality
    public void testUpdateDataStructure() throws IOException {
        ClashDetect cd = new ClashDetect();
//        retrieves initial files
        retrieveFiles(cd);

        String path = config.getString("testInitialDir") + "/" + config.getString("testInitialPack");
        File folder = new File(path);
        File f = folder.listFiles()[0];

//        deletes file
        boolean deleted = folder.listFiles()[0].delete();
        Assert.assertTrue(deleted);

//        updates the data structure
        update(cd);

//        adds the oracle values into list
        ArrayList<String> filesPresent = new ArrayList<String>();
        filesPresent.add("B.txt");
        filesPresent.add("C.txt");

        Map<String, String> fileNames = cd.listOfFiles();

//        Checks if everything in hash map is present in the oracle list
        for(Map.Entry<String, String> mp : fileNames.entrySet()){
            Assert.assertTrue(filesPresent.contains(mp.getKey()));
        }

        File file = new File(path + "/A.txt");
        file.createNewFile();
    }

    @Test
//    Tests if the add functionality works
    public void testAdd(){
        ClashDetect cd = new ClashDetect();
//        temp string files that will be added
        String[] tempFiles = {"Hello.java", "Bye.java", "Class.java", "Null.java"};
//        Creates non added files
        String[] nonFiles = {"HelloWorld.java", "ByeBye.java", "ClassAreGood.java", "NullNotFound.java"};

        for(String file: tempFiles){
            cd.addToMap(file, "TempDir");
        }

        HashMap<String, String> holder = cd.listOfFiles();

//        checks if they exist
        for (String file : tempFiles){
            Assert.assertTrue(holder.containsKey(file));
        }
//        checks if they dont exist
        for(String file : nonFiles){
            Assert.assertFalse(holder.containsKey(file));
        }
    }

    @Test
//    Tests the finding mechanism
    public void testFind(){
        ClashDetect cd = new ClashDetect();
//        temp string files that will be added
        String[] tempFiles = {"Hello.java", "Bye.java", "Class.java", "Null.java"};
//        Creates non added files
        String[] nonFiles = {"HelloWorld.java", "ByeBye.java", "ClassAreGood.java", "NullNotFound.java"};

//        Adds the files into the data structure
        for(String file: tempFiles){
            cd.addToMap(file, "TempDir");
        }

//        Does assertion on file and directory
        for(String file : tempFiles){
            Assert.assertTrue(cd.isFound(file, "TempDir"));
        }
        for(String file : tempFiles){
            Assert.assertFalse(cd.isFound(file, "Non_Existent_Dir"));
        }
        for(String file : nonFiles){
            Assert.assertFalse(cd.isFound(file, "TempDir"));
        }
    }

    @Test
//    Tests if clearing the data structure works
    public void testClearingDatasStructure(){
        ClashDetect cd = new ClashDetect();
//        retrieves initial files
        retrieveFiles(cd);

//        removes everything from data structure
        cd.removeAll();

        Map<String, String> ds = cd.listOfFiles();
        Assert.assertEquals(0, ds.size());
    }

    @Test
//    Tests that the removing of spaces in between is done correctly
    public void testRemovingSpace(){
        JTextField input = new JTextField();

        input.setText("Bob The Builder");
        Assert.assertTrue((input.getText().trim().replaceAll(" ", "")).equals("BobTheBuilder"));
        input.setText("Another One ");
        Assert.assertTrue((input.getText().trim().replaceAll(" ", "")).equals("AnotherOne"));
        input.setText("Luke");
        Assert.assertTrue((input.getText().trim().replaceAll(" ", "")).equals("Luke"));
        input.setText("");
        Assert.assertTrue((input.getText().trim().replaceAll(" ", "")).equals(""));
        input.setText(" ");
        Assert.assertTrue((input.getText().trim().replaceAll(" ", "")).equals(""));
    }

/*
########################################################################################################################
############################################# Homework 2 Test Cases ####################################################
########################################################################################################################
*/

    @Test
//    Tests to see if label gets created with the right information from label method
    public void testLabelCreation(){
        CustomInput mc = new CustomInput(){};

        JBLabel testing = mc.label("Test");
        Assert.assertEquals("Test", testing.getText());
        Assert.assertTrue(testing instanceof JBLabel);
    }

    @Test
//    Checks to see if main function returns a JPanel
    public void testPanelCreation(){
        MainContent mc = new MainContent(null, config.getString("testDir"), config.getString("testPackage"));

        JComponent testing = mc.main();
        Assert.assertTrue(testing instanceof JPanel);
    }


    private String pattern;
    @Test
    //    Tests whether the buttons do what I think they do
    public void testButtonEventListener(){

//        Action called when button is clicked
        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                pattern = ev.getActionCommand();
            }
        };

//        Creates the buttons for each design pattern
        JButton abstractFactory = new JButton("Abstract_Factory");
        JButton builder = new JButton("Builder");
        JButton chainOfResponsibility = new JButton("Chain_of_Responsibility");
        JButton facade = new JButton("Facade");
        JButton factoryMethod = new JButton("Factory_Method");
        JButton mediator = new JButton("Mediator");
        JButton templateMethod = new JButton("Template_Method");
        JButton visitor = new JButton("Visitor");

//        Sets the action listener to the buttons
        visitor.addActionListener(al);
        templateMethod.addActionListener(al);
        mediator.addActionListener(al);
        factoryMethod.addActionListener(al);
        facade.addActionListener(al);
        chainOfResponsibility.addActionListener(al);
        abstractFactory.addActionListener(al);
        builder.addActionListener(al);

//        Clicks button and checks the result of that action
        visitor.doClick();
        Assert.assertEquals("Visitor", pattern);
        templateMethod.doClick();
        Assert.assertEquals("Template_Method", pattern);
        mediator.doClick();
        Assert.assertEquals("Mediator", pattern);
        facade.doClick();
        Assert.assertEquals("Facade", pattern);
        factoryMethod.doClick();
        Assert.assertEquals("Factory_Method", pattern);
        abstractFactory.doClick();
        Assert.assertEquals("Abstract_Factory", pattern);
        chainOfResponsibility.doClick();
        Assert.assertEquals("Chain_of_Responsibility", pattern);
        builder.doClick();
        Assert.assertEquals("Builder", pattern);
    }

    @Test
//    These tests test methods specific to the Abstract Factory Class
    public void testAbstractFactoryClass(){
        AbstractFactory abs = new AbstractFactory();
        ClassName n = ClassName.get(config.getString("testPackage"), "Shapes");
        MethodSpec m = abs.absGenMethod("generateInstance", n, "shape");
        Assert.assertTrue(m instanceof MethodSpec);
    }

    @Test
//    Checks whether to see if there is any input in the textfield before continuing
    public void testRequirementOfInput(){
        String interName = "";
        int count = 0;
        JTextField textSimulation = new JTextField();
        do {
            textSimulation.setText(interName);
            interName = textSimulation.getText();
            count += 1;
            if(count == 5){
                interName = "NotEmpty";
            }
        }while(interName.equals(""));


        Assert.assertEquals(count, 5);
        Assert.assertEquals(interName, "NotEmpty");
    }

    private String fieldText;
    @Test
//    Tests that whatever the user inputs is whats read when the button is clicked
    public void testInputOfTextField(){
        JTextField textBox = new JTextField();
        JButton button = new JButton("Test");

//        Action called when button is clicked
        ActionListener action = new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                fieldText = textBox.getText();
            }
        };

        textBox.setText("Hello World");
        button.addActionListener(action);
        button.doClick();
        Assert.assertEquals("Hello World", fieldText);
    }

    @Test
//    Tests the method that retrieves path and directory
    public void testGettingDirectory(){

        String[] conf = new String[]{"", ""};
        try {
            InputStream inStream = TestCode.class.getResourceAsStream("/application.conf");
            if (inStream != null) {
                Reader reader = new InputStreamReader(inStream);

                // Load the configuration file
                Config tempConfig = ConfigFactory.parseReader(reader);

                // Load the destination directory for designs being generated
                conf[0] = tempConfig.getString("testDir");
                conf[1] = tempConfig.getString("testPackage");

                inStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] expectedOutput = new String[] {"./src/test/java", "output"};

        for(int i = 0; i < conf.length; i++){
            Assert.assertEquals(expectedOutput[i], conf[i]);
        }

    }



    /*
    ########################################################################################################################
    ############################################# Homework 1 Test Cases ####################################################
    ########################################################################################################################
     */
    @Test
//    Checks that class is not generated for misspelled design pattern
//    Test should not generate any classes or ask user any input
    public void testWrongInput(){
        gen = new Generator();

//        Nothing should be generated
        gen.generate("Facsakdaade", config.getString("test2Pack"), config.getString("testDir"));
//        Stores path of the created file if the file was created
        Path path = Paths.get(config.getString("testDir")+"/"+config.getString("test2Pack"));
        Assert.assertEquals(false, Files.exists(path));
    }

    @Test
//    Checks if a class was generated
    public void testClassGenerator(){
//        Creates instance of a pattern
        Pattern pat = new AbstractFactory();

        String[] testMethods = {"drawTest", "measureTest"};
        ClassName inter = ClassName.get(config.getString("testPackage"), "TestInterface");

//        Runs general class generator with an interface
        pat.genClass("test", pat.listMethods(testMethods, false, null, null),
                inter, false, config.getString("testPackage"), config.getString("testDir"));

//        Stores path of the created file if the file was created
        Path path = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/test.java");

//        Checks if the file exists by calling exist method of Files
        Assert.assertTrue(Files.exists(path));
    }

    @Test
//    Test functionality of generating empty and abstract methods
    public void testMethodStore(){
        String[] listOfMethods = {"draw", "measure"};
        Pattern pat = new AbstractFactory();
        ArrayList<MethodSpec> abstractMethods = pat.listAbsMethods(listOfMethods, null, null, null);
        ArrayList<MethodSpec> emptyMethds = pat.listMethods(listOfMethods, false, null, null);

        Assert.assertEquals(listOfMethods.length, abstractMethods.size());
        Assert.assertEquals(listOfMethods.length, emptyMethds.size());
    }

    @Test
//    These tests test methods specific to the Builder Class
    public void testBuilderClassMethod(){
        Builder b = new Builder();
        ClassName type = ClassName.get("testPackage", "Circle");
        ClassName type2 = ClassName.get("testPackage", "Shape");
        ClassName arrayList = ClassName.get("java.util", "ArrayList");
        ClassName list = ClassName.get("java.util", "List");
        ClassName co = ClassName.get("testPackage", "ComplexObjects");
        TypeName listInter = ParameterizedTypeName.get(list, type2);
        ArrayList<MethodSpec> ms = new ArrayList<MethodSpec>();

        MethodSpec m = b.genAddMethod(type);
        MethodSpec comM = b.genComplexConst(arrayList, type2);

        ms.add(m);
        ms.add(comM);

        Assert.assertTrue(m instanceof MethodSpec && comM instanceof MethodSpec);

        b.genComplexClass("ComplexObjects", listInter, ms, config.getString("testPackage"), config.getString("testDir"));
        Path path = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/ComplexObjects.java");
//        Checks if the file exists by calling exist method of Files
        Assert.assertTrue(Files.exists(path));

        ArrayList<MethodSpec> buildM = new ArrayList<MethodSpec>();

        MethodSpec bm = b.genBuilderMethods("build", type);
        MethodSpec conM = b.genConcreteCompMethod("gen", co);
        MethodSpec construct = b.genBuilderConstructor(co);

        buildM.add(bm);
        buildM.add(conM);
        buildM.add(construct);

        Assert.assertTrue(bm instanceof MethodSpec && conM instanceof MethodSpec && construct instanceof MethodSpec);

        b.genBuilderClass("Build", buildM, co, type2, config.getString("testPackage"), config.getString("testDir"));
        path = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/Build.java");
//        Checks if the file exists by calling exist method of Files
        Assert.assertTrue(Files.exists(path));
    }

    @Test
//    These tests test methods specific to the Facade Class
    public void testFacadeClass(){
        Facade f = new Facade();

        ClassName type = ClassName.get("testPackage", "Test1");
        ClassName type2 = ClassName.get("testPackage", "Test2");
        ArrayList<ClassName> paramType = new ArrayList<ClassName>();
        ArrayList<MethodSpec> m = new ArrayList<MethodSpec>();
        paramType.add(type);
        paramType.add(type2);

        MethodSpec consM = f.genConstructor(paramType, new String[]{"Test1", "Test2"});
        Assert.assertTrue(consM instanceof MethodSpec);

        m.add(consM);
        f.genClass("Fac", m, paramType, new String[]{"Test1", "Test2"}, config.getString("testPackage"), config.getString("testDir"));
        Path path = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/Fac.java");
//        Checks if the file exists by calling exist method of Files
        Assert.assertTrue(Files.exists(path));
    }

    @Test
//    These tests test methods specific to the Chain of Responsibility Class
    public void testChainOfResponsibility(){
        ChainOfResponsibility chain = new ChainOfResponsibility();

        ClassName type = ClassName.get(config.getString("testPackage"), "Chain");

        ArrayList<MethodSpec> absM = new ArrayList<MethodSpec>();
        MethodSpec handleM = chain.genHandleReq();
        MethodSpec constM = chain.genAbsConstructor("j", "testPackage");
        MethodSpec conClass = chain.classConstructor(type);

//        Checks if the return type is whats expected
        Assert.assertTrue(handleM instanceof MethodSpec && constM instanceof MethodSpec && conClass instanceof MethodSpec);

        chain.genInterface("Chain", absM, config.getString("testPackage"), config.getString("testDir"));
        Path path = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/Chain.java");
//        Checks if the file exists by calling exist method of Files
        Assert.assertTrue(Files.exists(path));
    }

    @Test
//    These tests test methods specific to the Factory Method Class
    public void testFactoryMethodClass(){
        FactoryMethod fm = new FactoryMethod();

        ClassName returnType = ClassName.get("testPackage", "Shape");
        String[] className = {"Circle", "Square"};
        ArrayList<ClassName> classType = new ArrayList<ClassName>();
        for(String s : className){
            classType.add(ClassName.get("testPackage", s));
        }
        MethodSpec m = fm.genFactoryMethod("factoryMethod", returnType, className, classType);

        Assert.assertTrue(m instanceof MethodSpec);

        fm.genFactoryClass("FactM", m, config.getString("testPackage"), config.getString("testDir"));
        Path path = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/FactM.java");
//        Checks if the file exists by calling exist method of Files
        Assert.assertTrue(Files.exists(path));
    }

    @Test
//    These tests test methods specific to the Mediator Class
    public void testMediatorClass(){

        Mediator med = new Mediator();

        ClassName inter = ClassName.get(config.getString("testPackage"), "Elements");
        ClassName medType = ClassName.get(config.getString("testPackage"), "Mediate");
        String[] classNames = {"ElemA", "ElemB"};
        ArrayList<MethodSpec> classM = new ArrayList<MethodSpec>();
        ArrayList<MethodSpec> mediatorM = new ArrayList<MethodSpec>();
        ArrayList<ClassName> fieldType = new ArrayList<ClassName>();

        for(String s : classNames){
            ClassName type = ClassName.get(config.getString("testPackage"), s);
            fieldType.add(type);
            mediatorM.add(med.genRegister("reg"+s, type, s.toLowerCase()));
        }
        for(MethodSpec m : mediatorM){
            Assert.assertTrue(m instanceof MethodSpec);
        }
        med.genMediatorClass("Mediate", mediatorM, inter, fieldType, classNames, config.getString("testPackage"), config.getString("testDir"));
        Path path = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/Mediate.java");
//        Checks if the file exists by calling exist method of Files
        Assert.assertTrue(Files.exists(path));

        classM.add(med.classConstructor(medType, "Mediator"));

        Assert.assertTrue(classM.get(0) instanceof MethodSpec);

        for(String s : classNames){
            med.genClasses(s, classM, inter, medType, "Mediator", config.getString("testPackage"), config.getString("testDir"));
        }

        Path classPath1 = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/ElemB.java");
        Path classPath2 = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/ElemA.java");
        Assert.assertTrue(Files.exists(classPath1));
        Assert.assertTrue(Files.exists(classPath2));
    }

    @Test
//    These tests test methods specific to the Visitor Class
    public void testVisitorClass(){
        Visitor v = new Visitor();
        ClassName type = ClassName.get(config.getString("testPackage"), "Visitor");

        Assert.assertTrue(v.genConcreteAccept(type) instanceof MethodSpec);
    }

    @Test
//    These tests test methods specific to the Template Method Class
    public void testTemplateMethodClass(){
        TemplateMethod tm = new TemplateMethod();
        String[] methods = {"first", "second", "third"};
        MethodSpec m = tm.genTemplateMethod(methods);
        ArrayList<MethodSpec> methodCollect = tm.listAbsMethods(methods, null, null, null);
        methodCollect.add(m);
        Assert.assertTrue(m instanceof MethodSpec);

        tm.genAbstractClass("TempMethod", methodCollect, config.getString("testPackage"), config.getString("testDir"));

        Path path = Paths.get(config.getString("testDir")+"/"+config.getString("testPackage")+"/TempMethod.java");
//        Checks if the file exists by calling exist method of Files
        Assert.assertTrue(Files.exists(path));
    }

    @Test
//    Test whether the factory returns what we expect it to return
    public void testDesignFactory(){
        DesignFactory f = new Factory();
        Assert.assertTrue(f.designPattern("Abstract_Factory") instanceof AbstractFactory);
        Assert.assertTrue(f.designPattern("Builder") instanceof Builder);
        Assert.assertTrue(f.designPattern("Chain_of_Responsibility") instanceof ChainOfResponsibility);
        Assert.assertTrue(f.designPattern("Facade") instanceof Facade);
        Assert.assertTrue(f.designPattern("Factory_Method") instanceof FactoryMethod);
        Assert.assertTrue(f.designPattern("Mediator") instanceof Mediator);
        Assert.assertTrue(f.designPattern("Template_Method") instanceof TemplateMethod);
        Assert.assertTrue(f.designPattern("Visitor") instanceof Visitor);
    }

    @Test
//    Tests the add functionality of composite
    public void testCompositeAdd(){
        AbstractFactory ab = new AbstractFactory();
        Composite c = new Composite();

        ArrayList<Pattern> pat = new ArrayList<Pattern>();
        c.add(ab);
        pat = c.patternsAdded();

//        Checks if it is added one time and also checks if it added the correct thing
        Assert.assertEquals(1, pat.size());
        Assert.assertTrue(pat.get(0) instanceof AbstractFactory);
    }
}
