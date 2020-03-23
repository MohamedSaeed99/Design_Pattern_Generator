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

public class CustomInterfaceInput extends DialogWrapper implements CustomInput {
    final static Logger log = LoggerFactory.getLogger(CustomInterfaceInput.class);
    public JPanel pan = new JPanel(new GridBagLayout());
    public JTextField interName = new JTextField();

    public CustomInterfaceInput(boolean canBeParent) {
        super(canBeParent);
        init();
        setTitle("Interface");
    }

    @Nullable
    @Override
//    Creates a centered dialog panel
    protected JComponent createCenterPanel() {
        GridBag gb = new GridBag()
                .setDefaultInsets(new Insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0).setDefaultFill(GridBagConstraints.HORIZONTAL);

        pan.setPreferredSize(new Dimension(300, 150));
        pan.add(label("Enter Interface Name"), gb.nextLine().next().weightx(0.2));
        pan.add(interName, gb.nextLine().next().weightx(0.8));

        log.info("Added components to panel");

        return pan;
    }

    //    Retrieves the information of a JTextField box
    public String getInterface() {
        return interName.getText();
    }
}