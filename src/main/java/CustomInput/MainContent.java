package CustomInput;

import generate.Generator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainContent implements ActionListener {
    final static Logger log = LoggerFactory.getLogger(MainContent.class);
    private JPanel pan = new JPanel(new GridLayout(4, 2));
    private String path;
    private String pack;

    //    Constructor to set path and package that's passed in
    public MainContent(String path, String pack){
        this.path = path;
        this.pack = pack;
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
        String designPattern = e.getActionCommand();

        log.info("Button clicked value: ", designPattern);

        gen.generate(designPattern, pack, path);
    }
}
