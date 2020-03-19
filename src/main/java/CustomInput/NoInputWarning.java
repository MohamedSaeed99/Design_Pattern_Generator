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

public class NoInputWarning extends DialogWrapper implements CustomInput {
    final static Logger log = LoggerFactory.getLogger(NoInputWarning.class);
    public JPanel pan = new JPanel(new GridBagLayout());

    public NoInputWarning(boolean canBeParent) {
        super(canBeParent);
        init();
        setTitle("Warning");
    }

    @Nullable
    @Override
//    creates a warning pop-up
    protected JComponent createCenterPanel() {
        GridBag gb = new GridBag()
                .setDefaultInsets(new Insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0).setDefaultFill(GridBagConstraints.HORIZONTAL);

        pan.setPreferredSize(new Dimension(200, 100));
        pan.add(label("Warning: Missing Input Detected!!!"), gb.nextLine().next().weightx(0.2));

        log.warn("Invalid input");

        return pan;
    }
}