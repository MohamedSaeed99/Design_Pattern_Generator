package CustomInput;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.ui.components.JBLabel;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import plugin.DesignGen;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class ClassMethodInput extends DialogWrapper implements CustomInput{
    //    declares panel with gridbaglayout
    final static Logger log = LoggerFactory.getLogger(ClassMethodInput.class);
    public JPanel pan = new JPanel(new GridBagLayout());
    public JTextField classNames  = new JTextField();
    public JTextField methodNames = new JTextField();

    public ClassMethodInput(boolean canBeParent, String interName) {
        super(canBeParent);
        init();
//        sets the title of the pop-up window
        if(interName == ""){
            setTitle("Custom Classes");
        }
        else {
            setTitle("Custom Classes For " + interName);
        }
    }

    @Nullable
    @Override
//    Creates a pop-up window in the center of the screen
    public JComponent createCenterPanel() {
        GridBag gb = new GridBag()
                .setDefaultInsets(new Insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0).setDefaultFill(GridBagConstraints.HORIZONTAL);

        pan.setPreferredSize(new Dimension(400, 200));
        pan.add(label("List of Classes (Space Separated)"), gb.nextLine().next().weightx(0.2));
        pan.add(classNames, gb.nextLine().next().weightx(0.8));
        pan.add(label("List of Methods (Space Separated)"), gb.nextLine().next().weightx(0.2));
        pan.add(methodNames, gb.nextLine().next().weightx(0.8));

        log.info("Added components to panel");

        return pan;
    }

    //    Retrieves the information inputted by the user
    public String getClasses(){
        return classNames.getText();
    }
    public String getMethods(){
        return methodNames.getText();
    }
}
