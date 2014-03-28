package ru.nsu.gordin;

import com.sun.deploy.util.ArrayUtil;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class DrawPanel extends JPanel implements MouseMotionListener, MouseWheelListener, ItemListener, ChangeListener{
    private JFrame frame;
    private JSpinner moveSpinner;
    private JCheckBox moveCheckBox;
    private boolean isMovable = true;
    private JSpinner scaleSpinner;
    private JCheckBox scaleCheckBox;
    private boolean isScrollable = true;
    private JButton okButton;
    private JButton cancelButton;
    private int pixelUnit;
    private int mouseX;
    private int mouseY;
    private int centerX;
    private int centerY;
    private int stepMove;
    private int tmpMove;
    private int tmpScale;
    private int stepScale;
    static final int STEP_MIN = 1;
    static final int SCALE_MIN = 1;
    static final int STEP_MAX = 100;
    static final int SCALE_MAX = 100;
    static final int STEP_INIT = 10;
    static final int SCALE_INIT = 10;
    static final int PIXEL_UNIT_MIN = 20;
    static final int PIXEL_UNIT_INIT = 20;
    private int radius = 5;
    private CanvasPanel canvasPanel;
    private BufferedImage canvas;

    public DrawPanel(JFrame frame) {
        super(new BorderLayout());
        this.frame = frame;
        moveSpinner = new JSpinner(new SpinnerNumberModel(STEP_INIT, STEP_MIN, STEP_MAX, 1));
        stepMove = STEP_INIT;
        scaleSpinner = new JSpinner(new SpinnerNumberModel(SCALE_INIT, SCALE_MIN, SCALE_MAX, 1));
        stepScale = SCALE_INIT;
        pixelUnit = PIXEL_UNIT_INIT;
//        moveSpinner.addChangeListener(this);
//        scaleSpinner.addChangeListener(this);
        moveCheckBox = new JCheckBox();
//        moveCheckBox.addItemListener(this);
        moveCheckBox.setSelected(true);
        scaleCheckBox = new JCheckBox();
//        scaleCheckBox.addItemListener(this);
        scaleCheckBox.setSelected(true);
        canvasPanel = new CanvasPanel();
        canvasPanel.addMouseMotionListener(this);
        canvasPanel.addMouseWheelListener(this);
        add(canvasPanel, BorderLayout.CENTER);
    }

    public void increase() {
        pixelUnit += stepScale;
        canvasPanel.repaint();
    }

    public void decrease() {
        if(pixelUnit - stepScale > PIXEL_UNIT_MIN)
            pixelUnit -= stepScale;
        canvasPanel.repaint();
    }

    public void moveLeft() {
        centerX -= stepMove;
        canvasPanel.repaint();
    }

    public void moveRight() {
        centerX += stepMove;
        canvasPanel.repaint();
    }

    public void moveUp() {
        centerY -= stepMove;
        canvasPanel.repaint();
    }

    public void moveDown() {
        centerY += stepMove;
        canvasPanel.repaint();
    }

    public void reset() {
        centerX = canvas.getWidth() / 2;
        centerY = canvas.getHeight() / 2;
        pixelUnit = PIXEL_UNIT_INIT;
        canvasPanel.repaint();
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        JSpinner source = (JSpinner)e.getSource();
        if(source == moveSpinner) {
            tmpMove = (Integer)source.getValue();
        }
        else if(source == scaleSpinner) {
            tmpScale = (Integer)source.getValue();
        }
    }

    public void showSettingDialog() {
        JDialog dialog = new JDialog(frame, "Setting", true);
        dialog.setLayout(new GridLayout(3, 1));
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
//        okButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
//        cancelButton.addActionListener(this);
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
    public void mouseDragged(MouseEvent e) {
        if(!isMovable) return;
//        System.out.println("dragged " + e.getX() + " " + e.getY());
        centerX += e.getX() - mouseX;
        centerY += e.getY() - mouseY;
        mouseX = e.getX();
        mouseY = e.getY();
        canvasPanel.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!isMovable) return;
//        System.out.println("moved " + e.getX() + " " + e.getY());
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(!isScrollable) return;
//        System.out.println(e.getWheelRotation());
        int step = stepScale * (-e.getWheelRotation());
        if(pixelUnit + step > PIXEL_UNIT_MIN) {
            pixelUnit += step;
        }
        else {
            pixelUnit = PIXEL_UNIT_INIT;
        }
        canvasPanel.repaint();
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

    protected class CanvasPanel extends JPanel {
        private boolean init = false;

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            drawPlane();
            drawFunc();
            g.drawImage(canvas, 0, 0, null);

        }

        public void drawPlane() {
            if(!init) {
                init = true;
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
            }
            int width = getWidth();
            int height = getHeight();
            for(int i = centerX % pixelUnit; i < width; i += pixelUnit) {
                if(i < 0) i += pixelUnit;
                for(int j = 0; j < height; j++) {
                    canvas.setRGB(i, j, Color.GRAY.getRGB());
                }
            }
            for(int i = centerY % pixelUnit; i < height; i += pixelUnit) {
                if(i < 0) i += pixelUnit;
                for(int j = 0; j < width; j++) {
                    canvas.setRGB(j, i, Color.GRAY.getRGB());
                }
            }
        }
        public void drawFunc() {
            int width = getWidth();
            int height = getHeight();

            Function f[] = {new Function() {
                @Override
                public double calc(double t) {
                    return (t*t / (t*t - 1));
                }
            },
            new Function() {
                @Override
                public double calc(double t) {
                    return ((t*t + 1) / (t + 2));
                }
            }};
            Function df[] = {new Function() {
                @Override
                public double calc(double t) {
                    double tmp = (t*t - 1);
                    return (2*t / (tmp*tmp));
                }
            },
            new Function() {
                @Override
                public double calc(double t) {
                    double tmp = t+2;
                    return (t*t + 4*t -1) / tmp*tmp;
                }
            }};
            double minX = -centerX * 1.0 / pixelUnit;
            double minY = -(height - centerY) * 1.0 / pixelUnit;
            double maxX = (width - centerX) * 1.0 / pixelUnit;
            double maxY = centerY * 1.0 / pixelUnit;

            double tyPlus = 0;
            double yuMinus = 0;
            double txMinus = 0;
            double txPlus = 0;
            double dt = 0;



            // t = (-infinity, -2)
            tyPlus = (minY + Math.sqrt((minY + 4) * (minY + 4) - 20)) / 2;
            yuMinus = (minY - Math.sqrt((minY + 4) * (minY + 4) - 20)) / 2;
            dt = 1.0 / (pixelUnit * Math.max(Math.abs(df[1].calc(tyPlus)), Math.abs(df[1].calc(yuMinus))));

            for (double i = yuMinus; i <= tyPlus; i += dt) {
                int x = centerX + (int) ( pixelUnit * f[0].calc(i));
                int y = centerY - (int) ( pixelUnit * f[1].calc(i));
                if (y < height && y >= 0
                        && x < width && x >= 0) {

                    canvas.setRGB(x, y, Color.CYAN.getRGB());
                }
            }

            // t = (-2, -1)
            txMinus = -1.0 * Math.sqrt(maxX / (maxX - 1));
            yuMinus = (maxY - Math.sqrt((maxY + 4) * (maxY + 4) - 20)) / 2;
            dt = 1.0 / (pixelUnit * Math.max(Math.abs(df[0].calc(txMinus)), Math.abs(df[1].calc(yuMinus))));
            for(double i = yuMinus; i <= txMinus; i += dt) {
                int x = centerX + (int) ( pixelUnit * f[0].calc(i));
                int y = centerY - (int) ( pixelUnit * f[1].calc(i));
                if (y < height && y >= 0
                        && x < width && x >= 0) {
                    canvas.setRGB(x, y, Color.CYAN.getRGB());
                }
            }

            // t = (-1, 1)
            txPlus = Math.sqrt(minX / (minX - 1));
            txMinus = -1 * txPlus;
            dt = 1.0 / (pixelUnit * Math.max(Math.abs(df[0].calc(txPlus)), Math.abs(df[0].calc(txMinus))));
            for(double i = txMinus + dt; i <= txPlus; i += dt) {
                int x = centerX + (int) ( pixelUnit * f[0].calc(i));
                int y = centerY - (int) ( pixelUnit * f[1].calc(i));
                if (y < height && y >= 0
                        && x < width && x >= 0) {
                    canvas.setRGB(x, y, Color.CYAN.getRGB());
                }
            }

            // t = (1, infinity)
            txPlus = 1.* Math.sqrt(maxX / (maxX - 1));
            tyPlus = (maxY + Math.sqrt((maxY + 4) * (maxY + 4) - 20)) / 2;
            dt = 1.0 / (pixelUnit * Math.max(Math.abs(df[1].calc(tyPlus)), Math.abs(df[0].calc(txPlus))));
            for(double i = txPlus + dt; i <= tyPlus; i += dt) {
                int x = centerX + (int) ( pixelUnit * f[0].calc(i));
                int y = centerY - (int) ( pixelUnit * f[1].calc(i));
                if (y < height && y >= 0
                        && x < width && x >= 0) {
                    canvas.setRGB(x, y, Color.CYAN.getRGB());
                }
            }
        }
    }
}
