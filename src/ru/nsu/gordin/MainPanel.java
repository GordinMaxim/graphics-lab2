package ru.nsu.gordin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class MainPanel extends JPanel{
    protected Action aboutAction, exitAction, settingAction,
                     moveLeftAction, moveRightAction, moveUpAction, moveDownAction,
                     increaseAction, decreaseAction;

    public MainPanel() {
        super(new BorderLayout());
    }

    public void initActions(DrawPanel panel) {
        aboutAction = new AboutAction("About",
                createToolIcon("info_icon&16"),
                "About lab1",
                new Integer(KeyEvent.VK_H), getRootPane());
        exitAction = new ExitAction("Exit",
                createToolIcon("on-off_icon&16"),
                "Quit the application",
                new Integer(KeyEvent.VK_E));
        moveLeftAction = new MoveLeftAction("Move left",
                createToolIcon("arrow_left_icon&16"),
                "Move left",
                new Integer(KeyEvent.VK_LEFT), panel);
        moveRightAction = new MoveRightAction("Move right",
                createToolIcon("arrow_right_icon&16"),
                "Move Right",
                new Integer(KeyEvent.VK_RIGHT), panel);
        moveUpAction = new MoveUpAction("Move up",
                createToolIcon("arrow_top_icon&16"),
                "Move Up",
                new Integer(KeyEvent.VK_UP), panel);
        moveDownAction = new MoveDownAction("Move down",
                createToolIcon("arrow_bottom_icon&16"),
                "Move Down",
                new Integer(KeyEvent.VK_DOWN), panel);
        increaseAction = new IncreaseAction("Increase",
                createToolIcon("round_plus_icon&16"),
                "Increase",
                new Integer(KeyEvent.VK_PLUS), panel);
        decreaseAction = new DecreaseAction("Decrease",
                createToolIcon("round_minus_icon&16"),
                "Decrease",
                new Integer(KeyEvent.VK_MINUS), panel);
        settingAction = new SettingAction("Setting",
                createToolIcon("cog_icon&16"),
                "Setting",
                new Integer(KeyEvent.VK_S), panel);
    }

    protected static ImageIcon createToolIcon(String imageName) {
        String imgLocation = "/res/icon/"
                + imageName
                + ".png";
        java.net.URL imageURL = MainPanel.class.getResource(imgLocation);
        System.out.println();
        if (imageURL == null) {
            System.err.println("Resource not found: "
                    + imgLocation);
            return null;
        } else {
            return new ImageIcon(imageURL);
        }
    }

    public JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        menuBar.add(createViewMenu());
        menuBar.add(createHelpMenu());
        return menuBar;
    }

    public JMenu createFileMenu() {
        JMenuItem menuItem = null;
        JMenu fileMenu = new JMenu("File");

        Action[] actions = {settingAction, exitAction};
        for (int i = 0; i < actions.length; i++) {
            menuItem = new JMenuItem(actions[i]);
            menuItem.setIcon(null); //arbitrarily chose not to use icon
            fileMenu.add(menuItem);
        }

        return fileMenu;
    }

    public JMenu createViewMenu() {
        JMenuItem menuItem = null;
        JMenu viewMenu = new JMenu("View");

        Action[] actions = {moveLeftAction, moveRightAction, moveUpAction, moveDownAction,
                increaseAction, decreaseAction};
        for (int i = 0; i < actions.length; i++) {
            menuItem = new JMenuItem(actions[i]);
            menuItem.setIcon(null); //arbitrarily chose not to use icon
            viewMenu.add(menuItem);
        }

        return viewMenu;
    }

    public JMenu createHelpMenu() {
        JMenuItem menuItem = null;
        JMenu helpMenu = new JMenu("Help");

        Action[] actions = {aboutAction};
        for (int i = 0; i < actions.length; i++) {
            menuItem = new JMenuItem(actions[i]);
            menuItem.setIcon(null);
            helpMenu.add(menuItem);
        }

        return helpMenu;
    }

    public void createToolBar() {
        JButton button = null;

        ToolBar toolBar = new ToolBar();
        add(toolBar, BorderLayout.PAGE_START);

        Action[] actions = {moveLeftAction, moveRightAction, moveUpAction, moveDownAction,
                increaseAction, decreaseAction, settingAction, aboutAction, exitAction};

        for(int i = 0; i < actions.length; i++){
            button = new JButton(actions[i]);
            if (button.getIcon() != null) {
                button.setText("");
            }
            toolBar.add(button);
        }
    }




    private static void createAndShowGUI() {
        JFrame frame = new JFrame("lab #1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainPanel demo = new MainPanel();
        DrawPanel drawPanel = new DrawPanel(frame);
        demo.initActions(drawPanel);
        frame.setJMenuBar(demo.createMenuBar());
        demo.createToolBar();
        demo.add(drawPanel);
        demo.setOpaque(true);
        frame.setContentPane(demo);
        frame.setMinimumSize(new Dimension(800, 600));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}