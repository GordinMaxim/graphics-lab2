package ru.nsu.gordin;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class MoveLeftAction extends AbstractAction {
    private DrawPanel panel;

    public MoveLeftAction(String text, ImageIcon icon,
                          String desc, Integer mnemonic, DrawPanel panel) {
        super(text, icon);
        putValue(SHORT_DESCRIPTION, desc);
        putValue(MNEMONIC_KEY, mnemonic);
        this.panel = panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        panel.moveLeft();
    }
}