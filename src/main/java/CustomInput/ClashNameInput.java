package CustomInput;

import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.uiDesigner.core.AbstractLayout;
import com.intellij.util.ui.GridBag;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class ClashNameInput extends DialogWrapper implements CustomInput {
    final static Logger log = LoggerFactory.getLogger(ClashNameInput.class);

    public JPanel pan = new JPanel(new GridBagLayout());
    public JTextField rename = new JTextField();

    String name;

    public ClashNameInput(boolean canBeParent, String name) {
        super(canBeParent);
        this.name = name;
        init();
        setTitle("Rename " + name);
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        GridBag gb = new GridBag()
                .setDefaultInsets(new Insets(0, 0, AbstractLayout.DEFAULT_VGAP, AbstractLayout.DEFAULT_HGAP))
                .setDefaultWeightX(1.0).setDefaultFill(GridBagConstraints.HORIZONTAL);

        pan.setPreferredSize(new Dimension(200, 100));
        String txt = "Class with name " + this.name+" already exists";
        pan.add(label(txt), gb.nextLine().next().weightx(0.2));
        pan.add(rename, gb.nextLine().next().weightx(0.8));

        log.info("Added components to panel");

        return pan;
    }

//    Retrieves information from the text field
    public String getName() {
        return rename.getText();
    }
}
