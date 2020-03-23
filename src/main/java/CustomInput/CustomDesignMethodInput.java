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

public class CustomDesignMethodInput extends DialogWrapper implements CustomInput {
    final static Logger log = LoggerFactory.getLogger(CustomDesignMethodInput.class);

    public JPanel pan = new JPanel(new GridBagLayout());
    public JTextField designMethod = new JTextField();
    String name;

    public CustomDesignMethodInput(boolean canBeParent, String design) {
        super(canBeParent);
        this.name = design;
//        initializes the pop-up
        init();
//        sets the title
        setTitle(design);
    }

    @Nullable
    @Override
//    Generates a pop-up window in the center of the screen
    protected JComponent createCenterPanel() {
        GridBag gb = new GridBag()
                .setDefaultInsets(new Insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0).setDefaultFill(GridBagConstraints.HORIZONTAL);

        pan.setPreferredSize(new Dimension(300, 150));
        pan.add(label("Enter Method Names for "+this.name+" (Space Separated)"), gb.nextLine().next().weightx(0.2));
        pan.add(designMethod, gb.nextLine().next().weightx(0.8));

        log.info("Added components to panel");

        return pan;
    }


    //    Retrieves information from the text field
    public String getMethod() {
        return designMethod.getText();
    }
}