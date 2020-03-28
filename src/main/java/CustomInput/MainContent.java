package CustomInput;

import com.intellij.openapi.project.Project;
import generate.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import plugin.ClashDetect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainContent implements ActionListener {
    final static Logger log = LoggerFactory.getLogger(MainContent.class);
    private JPanel pan = new JPanel(new GridLayout(4, 2));
    private String path;
    private String pack;
    private Project project;

    //    Constructor to set path and package that's passed in
    public MainContent(Project project, String path, String pack){
        this.path = path;
        this.pack = pack;
        this.project = project;
    }

    //    creates a panel from the buttons
    public JComponent main (){
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
        visitor.addActionListener(this);
        templateMethod.addActionListener(this);
        mediator.addActionListener(this);
        factoryMethod.addActionListener(this);
        facade.addActionListener(this);
        chainOfResponsibility.addActionListener(this);
        abstractFactory.addActionListener(this);
        builder.addActionListener(this);

//        Adds the buttons into the panel
        pan.add(abstractFactory);
        pan.add(builder);
        pan.add(chainOfResponsibility);
        pan.add(facade);

        pan.add(factoryMethod);
        pan.add(mediator);
        pan.add(templateMethod);
        pan.add(visitor);

        log.info("Added components to panel");

        return pan;
    }

    @Override
//    Listens for the button presses and executes them
    public void actionPerformed(ActionEvent e) {
        Generator gen = new Generator();
//        gets the absolute directory where files well be stored
        String path = this.path + "/" + this.pack.replaceAll("\\.", "/");

//        sets up the initial hashmap with the files already there
//        allows for clean set of files when button is pressed
//        makes it able to check if files were deleted or not
        ClashDetect cd = new ClashDetect(path, this.project);
        cd.updateFiles();
        String designPattern = e.getActionCommand();

        log.info("Button clicked value: {}", designPattern);

//        Gives a percise name to the package
        String dir = path.substring(path.lastIndexOf("/src")+5).replaceAll("/", ".");
        path = path.substring(0, path.lastIndexOf("/src")+4);

        log.info("Path: {}", path);
        log.info("Package: {}", dir);

        gen.generate(designPattern, dir, path);
    }
}
