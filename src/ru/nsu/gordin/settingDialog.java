package ru.nsu.gordin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class settingDialog extends JDialog implements ActionListener {
    private JSpinner moveSpinner;
    private JCheckBox moveCheckBox;
    private boolean isMovable = true;
    private JSpinner scaleSpinner;
    private JCheckBox scaleCheckBox;
    private boolean isScrollable = true;
    private JButton okButton;
    private JButton cancelButton;

    public settingDialog(JFrame frame) {
        super();
        JDialog dialog = new JDialog(frame, "Setting", true);
        dialog.setLayout(new GridLayout(3, 1));
        //        moveSpinner.addChangeListener(this);
//        scaleSpinner.addChangeListener(this);

//        moveCheckBox.addItemListener(this);
        moveCheckBox.setSelected(true);
        scaleCheckBox = new JCheckBox();
//        scaleCheckBox.addItemListener(this);
        scaleCheckBox.setSelected(true);
        JPanel movePanel = new JPanel(new GridLayout(2, 2));
        movePanel.setBorder(new TitledBorder("Move"));
        movePanel.add(new JLabel("move step"));
        movePanel.add(moveSpinner);
        movePanel.add(new JLabel("mouse move"));

        movePanel.add(moveCheckBox);
        JPanel scalePanel = new JPanel(new GridLayout(2, 2));
        scalePanel.setBorder(new TitledBorder("Scale"));
        scalePanel.add(new JLabel("scale step"));
        scalePanel.add(scaleSpinner);
        scalePanel.add(new JLabel("scroll scale"));
        scalePanel.add(scaleCheckBox);
        JPanel buttonPanel = new JPanel();
        okButton = new JButton("OK");
        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        dialog.add(movePanel);
        dialog.add(scalePanel);
        dialog.add(buttonPanel);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setResizable(false);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}
