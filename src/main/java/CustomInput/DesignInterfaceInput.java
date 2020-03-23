package CustomInput;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class DesignInterfaceInput extends DialogWrapper implements CustomInput  {
    final static Logger log = LoggerFactory.getLogger(CustomDesignMethodInput.class);

    public JPanel pan = new JPanel(new GridBagLayout());
    public JTextField dInter = new JTextField();
    public JTextField info = new JTextField();

    String name;
    boolean addInfo;
    public DesignInterfaceInput(boolean canBeParent, String name, boolean addInfo) {
        super(canBeParent);
        this.name = name;
        this.addInfo = addInfo;
        init();
        setTitle(name);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        GridBag gb = new GridBag()
                .setDefaultInsets(new Insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0).setDefaultFill(GridBagConstraints.HORIZONTAL);

        pan.setPreferredSize(new Dimension(400, 200));
        String txt = "Enter Interface for "+this.name;
        pan.add(label(txt), gb.nextLine().next().weightx(0.2));
        pan.add(dInter, gb.nextLine().next().weightx(0.8));

        if(addInfo) {
            String infoText = "Enter Class Name of Helper for " + this.name;
            pan.add(label(infoText), gb.nextLine().next().weightx(0.2));
            pan.add(info, gb.nextLine().next().weightx(0.8));
        }

        log.info("Added components to panel");

        return pan;
    }

    //    Retrieves information from the text field
    public String getName() {
        return dInter.getText();
    }
    //    Retrieves information from the text field
    public String getInfo() {
        return info.getText();
    }
}
