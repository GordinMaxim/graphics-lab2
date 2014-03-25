package ru.nsu.gordin;

import javax.accessibility.AccessibleContext;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.image.BufferedImage;

public class DrawPanel extends JPanel implements ChangeListener {
    private JFrame frame;
    private JSpinner moveSpinner;
    private JSpinner scaleSpinner;
    private int pixelUnit;
    private int centerX;
    private int centerY;
    private int stepMove;
    private int stepScale;
    static final int STEP_MIN = 1;
    static final int SCALE_MIN = 1;
    static final int STEP_MAX = 100;
    static final int SCALE_MAX = 100;
    static final int STEP_INIT = 10;
    static final int SCALE_INIT = 10;
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
        pixelUnit = SCALE_INIT;
        moveSpinner.addChangeListener(this);
        scaleSpinner.addChangeListener(this);
        canvasPanel = new CanvasPanel();
        add(canvasPanel, BorderLayout.CENTER);
    }

    public void increase() {
        pixelUnit += stepScale;
        canvasPanel.repaint();
    }

    public void decrease() {
        if(pixelUnit > stepScale)
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

    public void showSettingDialog() {
        JDialog dialog = new JDialog(frame, "Setting", true);
        dialog.setLayout(new GridLayout(2, 2));
        dialog.add(new JLabel("move step"));
        dialog.add(moveSpinner);
        dialog.add(new JLabel("scale step"));
        dialog.add(scaleSpinner);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    protected class CanvasPanel extends JPanel {

        private boolean show = true;
        private boolean init = false;

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            drawPlane(g);
            if(show) {
                drawCircle(g);
            }
        }

        public void drawPlane(Graphics g) {
            if(!init) {
                init = true;
                centerX = getWidth() / 2;
                centerY = getHeight() / 2;
            }
            Graphics2D g2 = (Graphics2D)g;
            int width = getWidth();
            int height = getHeight();
            for(int i = centerX % pixelUnit; i < width; i += pixelUnit) {
                for(int j = 0; j < height; j++) {
                    canvas.setRGB(i, j, Color.GRAY.getRGB());
                }
            }
            for(int i = centerY % pixelUnit; i < height; i += pixelUnit) {
                for(int j = 0; j < width; j++) {
                    canvas.setRGB(j, i, Color.GRAY.getRGB());
                }
            }
        }

        public void drawCircle(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            int height = getHeight();
            int width = getWidth();
            int x0 = centerX, y0 = centerY;
            int x = 0, y = radius*pixelUnit;
            int delta = 2 * (1 - radius*pixelUnit);
            int error = 0;
            while(0 <= y) {
                canvas.setRGB(x0 + x, y0 + y, Color.CYAN.getRGB());
                canvas.setRGB(x0 - x, y0 + y, Color.CYAN.getRGB());
                canvas.setRGB(x0 + x, y0 - y, Color.CYAN.getRGB());
                canvas.setRGB(x0 - x, y0 - y, Color.CYAN.getRGB());
                error = 2 * (delta + y) - 1;
                if(delta < 0 && error <= 0) {
                    x++;
                    delta += 2 * x + 1;
                    continue;
                }
                error = 2 * (delta - x) - 1;
                if(delta > 0 && error > 0) {
                    y--;
                    delta += 1 - 2 * y;
                    continue;
                }
                x++;
                delta += 2 * (x - y);
                y--;
            }

            g2.drawImage(canvas, 0, 0, null);
        }
    }
}
