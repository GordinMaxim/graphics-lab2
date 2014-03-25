package ru.nsu.gordin;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AboutAction extends AbstractAction {
    private JRootPane rootPane;

    public AboutAction(String text, ImageIcon icon,
                       String desc, Integer mnemonic, JRootPane rootPane) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.rootPane = rootPane;
    }

    public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(rootPane,"Gordin Maxim, gormakc.ru@gmail.com");
    }
}