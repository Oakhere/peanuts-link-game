import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainGUI extends JFrame {

    public MainGUI() {
        setTitle("花生连连看");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame on the screen

        // Set background image
        ImageIcon backgroundImage = new ImageIcon("background.jpg"); // Change "background.jpg" to the path of your image
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setLayout(new BorderLayout());

        // Create buttons on the left
        JPanel leftPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        leftPanel.setOpaque(false); // Make the panel transparent
        JButton basicModeButton = new JButton("基本模式");
        JButton casualModeButton = new JButton("休闲模式");
        JButton levelModeButton = new JButton("关卡模式");
        leftPanel.add(basicModeButton);
        leftPanel.add(casualModeButton);
        leftPanel.add(levelModeButton);

        // Create buttons on the right bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false); // Make the panel transparent
        JButton leaderboardButton = new JButton("排行榜");
        JButton settingsButton = new JButton("设置");
        JButton helpButton = new JButton("帮助");
        bottomPanel.add(leaderboardButton);
        bottomPanel.add(settingsButton);
        bottomPanel.add(helpButton);

        // Add panels to the background label
        backgroundLabel.add(leftPanel, BorderLayout.WEST);
        backgroundLabel.add(bottomPanel, BorderLayout.SOUTH);

        // Add background label to the frame
        add(backgroundLabel);

        // Set the frame visible
        setVisible(true);

        // Add action listeners to buttons
        basicModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close current MainGUI window
                dispose();
                // Open BasicModeGUI window
                BasicModeGUI basicModeGUI = new BasicModeGUI();
                basicModeGUI.setVisible(true);
            }
        });

        casualModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close current MainGUI window
                dispose();
                // Open BasicModeGUI window
                CasualModeGUI casualModeGUI = new CasualModeGUI();
                casualModeGUI.setVisible(true);            }
        });

        levelModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                LevelModeGUI levelModeGUI = new LevelModeGUI();
                levelModeGUI.setVisible(true);
            }
        });

        leaderboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add action for leaderboard button
            }
        });

        settingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Add action for settings button
            }
        });

        helpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = "Welcome to the PEANUTS link game!\n\n"
                        + "You can eliminate two same images that can be connected by at most 3 lines through clicking them.\n\n"
                        + "Here is a walk-through of the game system:\n\n"
                        + "1. Basic Mode: You have 5 min to eliminate all the images. If the time runs out before you eliminate all the images, you lose the game. You can pause the game by clicking the “pause” button. You can get hints by clicking the “hint” button. You can shuffle all the remaining images by clicking the “shuffle” button.\n\n"
                        + "2. Casual Mode: You have infinite time to eliminate all the images, so no need to hurry. You can get hints by clicking the “hint” button. You can shuffle all the remaining images by clicking the “shuffle” button.\n\n"
                        + "3. Level Mode: You can win the game by passing all 5 levels. For each level, you have 5 min. You can pause the game by clicking the “pause” button. You can get hints by clicking the “hint” button. You can shuffle all the remaining images by clicking the “shuffle” button.\n\n"
                        + "Have fun! 🐾";

                // Load the image
                ImageIcon icon = new ImageIcon("background2.jpg");

                // Create a JLabel to display the image
                JLabel label = new JLabel(icon);
                label.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the image

                // Create a JTextArea to display the message
                JTextArea textArea = new JTextArea(message);
                textArea.setEditable(false);
                textArea.setLineWrap(true);
                textArea.setWrapStyleWord(true);
                textArea.setColumns(50); // Set the number of columns for the text area
                textArea.setRows(15); // Set the number of rows for the text area

                // Create a JScrollPane to contain the text area
                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align the scroll pane

                // Create a JPanel to hold the image and the message
                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Use BoxLayout with vertical orientation
                panel.add(scrollPane); // Add the scroll pane containing the text area
                panel.add(Box.createVerticalStrut(10)); // Add some vertical space between components
                panel.add(label); // Add the label containing the image

                // Show the message dialog with the panel
                JOptionPane.showMessageDialog(MainGUI.this, panel, "Game Introduction", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainGUI();
            }
        });
    }
}
