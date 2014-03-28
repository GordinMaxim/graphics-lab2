package ru.nsu.gordin;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class SettingDialog extends JDialog implements ActionListener, ChangeListener, ItemListener{
    DrawPanel panel;
    private JSpinner moveSpinner;
    private JCheckBox moveCheckBox;
    private boolean isMovable;
    private int stepMove;
    private JSpinner scaleSpinner;
    private JCheckBox scaleCheckBox;
    private boolean isScrollable;
    private int stepScale;
    private JButton okButton;
    private JButton cancelButton;

    public SettingDialog(JFrame frame, DrawPanel panel) {
        super(frame, "Setting", true);
        this.panel = panel;
        isMovable = panel.isMovable();
        isScrollable = panel.isScrollable();
        stepMove = panel.getStepMove();
        stepScale = panel.getStepScale();
        setLayout(new GridLayout(3, 1));
        moveCheckBox = new JCheckBox();
        moveCheckBox.addItemListener(this);
        moveCheckBox.setSelected(isMovable);
        scaleCheckBox = new JCheckBox();
        scaleCheckBox.addItemListener(this);
        scaleCheckBox.setSelected(isScrollable);
        moveSpinner = new JSpinner(new SpinnerNumberModel(stepMove, DrawPanel.STEP_MIN, DrawPanel.STEP_MAX, 1));
        moveSpinner.addChangeListener(this);
        JPanel movePanel = new JPanel(new GridLayout(2, 2));
        movePanel.setBorder(new TitledBorder("Move"));
        movePanel.add(new JLabel("move step"));
        movePanel.add(moveSpinner);
        movePanel.add(new JLabel("mouse move"));
        movePanel.add(moveCheckBox);
        scaleSpinner = new JSpinner(new SpinnerNumberModel(stepScale, DrawPanel.SCALE_MIN, DrawPanel.SCALE_MAX, 1));
        scaleSpinner.addChangeListener(this);
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
        add(movePanel);
        add(scalePanel);
        add(buttonPanel);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        pack();
        setResizable(false);
        setLocationRelativeTo(this);
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        JCheckBox source = (JCheckBox)e.getSource();
        if(source == scaleCheckBox) {
            isScrollable = (ItemEvent.SELECTED == e.getStateChange());
        }
        else if(source == moveCheckBox){
            isMovable = (ItemEvent.SELECTED == e.getStateChange());
        }
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner source = (JSpinner)e.getSource();
        if(source == moveSpinner) {
            stepMove = (Integer)source.getValue();
        }
        else if(source == scaleSpinner) {
            stepScale = (Integer)source.getValue();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JButton source = (JButton)e.getSource();
        if(source == okButton) {
            System.out.println(stepMove + " " + stepScale);
            panel.setStepMove(stepMove);
            panel.setStepScale(stepScale);
            panel.setMovable(isMovable);
            panel.setScrollable(isScrollable);
        }
//        else if(source == cancelButton) {
//            stepMove = panel.getStepMove();
//            stepScale = panel.getStepScale();
//            isMovable = panel.isMovable();
//            isScrollable = panel.isScrollable();
//        }
        dispose();
    }
}
