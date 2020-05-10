package calebowusuyianoma.sortalgovisualiser;

import javax.swing.JOptionPane;
import java.awt.Component;

public class DefaultOptionPane implements OptionPane {
    @Override
    public void showMessageDialog(Component parentComponent, Object message) {
        JOptionPane.showMessageDialog(parentComponent, message);
    }
}
