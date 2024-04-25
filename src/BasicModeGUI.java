import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class BasicModeGUI extends JFrame {
    private final JButton startButton;
    private final JButton pauseButton;
    private final JButton hintButton;
    private final JButton shuffleButton;

    private final JPanel mapPanel; // Panel to display the map
    private final JLabel timerLabel; // Label to display the timer

    private Map mapObject; // Reference to the Map object

    private final Timer timer; // Timer for countdown
    private int remainingTime; // Remaining time in seconds

    private JLabel selectedLabel1; // First selected image label
    private JLabel selectedLabel2; // Second selected image label
    private JLabel hintLabel1;
    private JLabel hintLabel2;
    private int remainingVertices; // Counter for remaining vertices
    private boolean gamePaused; // Flag to indicate if the game is paused


    public BasicModeGUI() {
        setTitle("欢乐连连看——基本模式");
        setSize(800, 600); // Adjusted window size for map display
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create buttons for game functionalities
        startButton = new JButton("开始游戏");
        pauseButton = new JButton("暂停/继续");
        hintButton = new JButton("提示");
        shuffleButton = new JButton("重排");

        // Create buttons for additional functionalities
        JButton settingsButton = new JButton("设置");
        JButton helpButton = new JButton("帮助");

        // Set layout for the basic mode window
        setLayout(new BorderLayout());

        // Create panel for game functionalities buttons
        JPanel functionPanel = new JPanel();
        functionPanel.setLayout(new GridLayout(4, 1));
        functionPanel.add(startButton);
        functionPanel.add(pauseButton);
        functionPanel.add(hintButton);
        functionPanel.add(shuffleButton);

        // Add function panel to the right
        add(functionPanel, BorderLayout.EAST);

        // Create panel for displaying the map
        mapPanel = new JPanel(new GridLayout(10, 16)); // 16x10 grid layout for map display
        add(mapPanel, BorderLayout.CENTER);

        // Create panel for additional functionalities buttons and timer
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // Create panel for timer
        JPanel timerPanel = new JPanel();
        timerLabel = new JLabel("Remaining Time: 05:00"); // Initial label
        timerPanel.add(timerLabel);
        bottomPanel.add(timerPanel, BorderLayout.WEST);

        // Create panel for additional functionality buttons
        JPanel additionalPanel = new JPanel();
        additionalPanel.setLayout(new GridLayout(1, 2));
        additionalPanel.add(settingsButton);
        additionalPanel.add(helpButton);
        bottomPanel.add(additionalPanel, BorderLayout.EAST);

        // Add bottom panel to the bottom
        add(bottomPanel, BorderLayout.SOUTH);

        // Create a new Map object
        mapObject = new Map();
        remainingVertices = mapObject.map.length * mapObject.map[0].length;
        mapObject.shuffle(50);

        // Initialize timer
        remainingTime = 300; // 300 seconds = 5 minutes
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                remainingTime--;
                // Update the timer label
                int minutes = remainingTime / 60;
                int seconds = remainingTime % 60;
                String timeString = String.format("%02d:%02d", minutes, seconds);
                timerLabel.setText("Remaining Time: " + timeString);

                if (remainingTime <= 0) {
                    // Game over, stop the timer
                    timer.stop();
                    pauseButton.setEnabled(false);
                    setMouseListenersEnabled(false);
                    // Show game over message
                    JOptionPane.showMessageDialog(BasicModeGUI.this, "Game Over!");
                }
            }
        });

        // Add action listeners to buttons
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawMap();
                // Add mouse listener to the labels representing images
                setMouseListenersEnabled(true);
                // Start the timer
                timer.start();
                startButton.setEnabled(false);
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Toggle the gamePaused flag
                gamePaused = !gamePaused;

                // Disable/enable mouse event listeners based on gamePaused flag
                setMouseListenersEnabled(!gamePaused);

                // Pause/resume the timer based on gamePaused flag
                if (gamePaused) {
                    timer.stop();
                } else {
                    timer.start();
                }
            }
        });

        shuffleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mapObject.shuffle(50);
                drawMap();
                // Add mouse listener to the labels representing images
                setMouseListenersEnabled(!gamePaused);
            }
        });

        hintButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vertex[] pair = mapObject.hint();
                hintLabel1 = getLabelForVertex(pair[0]);
                hintLabel2 = getLabelForVertex(pair[1]);
                if (hintLabel1 != null && hintLabel2 != null) {
                    hintLabel1.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                    hintLabel2.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                }
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
                JOptionPane.showMessageDialog(BasicModeGUI.this, panel, "Game Introduction", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Remove the default close operation
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Add window listener to handle window closing event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Reopen MainGUI when BasicModeGUI is closed
                try {
                    MainGUI mainGUI = new MainGUI();
                    mainGUI.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Handle any exceptions that occur when reopening MainGUI
                }
            }
        });

    }

    // Method to draw the map
    private void drawMap() {
        mapPanel.removeAll(); // Clear previous map

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 16; col++) {
                Vertex vertex = mapObject.map[row][col];
                if (vertex.enabled) {
                    try {
                        // Load image based on vertex info
                        BufferedImage image = ImageIO.read(new File("images/image" + (vertex.info + 1) + ".jpg"));
                        JLabel label = new JLabel(new ImageIcon(image));
                        label.setBorder(BorderFactory.createEmptyBorder()); // Remove default border
                        label.setFocusable(false); // Ensure label is not focusable
                        mapPanel.add(label);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Display empty label for disabled vertices
                    mapPanel.add(new JLabel());
                }
            }
        }

        // Update the layout
        mapPanel.revalidate();
        mapPanel.repaint();
    }

    // Method to enable/disable mouse event listeners for image labels
    private void setMouseListenersEnabled(boolean enabled) {
        for (Component component : mapPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                if (enabled) {
                    // Add mouse listener if enabled
                    label.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            if (hintLabel1 != null && hintLabel2 != null) {
                                hintLabel1.setBorder(null);
                                hintLabel2.setBorder(null);
                                hintLabel1 = null;
                                hintLabel2 = null;
                            }
                            if (label.isEnabled()) {
                                if (selectedLabel1 == null) {
                                    selectedLabel1 = label;
                                    label.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                                } else if (selectedLabel2 == null && !label.equals(selectedLabel1)) {
                                    selectedLabel2 = label;
                                    label.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                                    // Check if the selected vertices can be linked
                                    if (canBeLinked(selectedLabel1, selectedLabel2)) {
                                        // If there's a legal path, perform the action
                                        drawPath();
                                        try {
                                            // Introduce a delay of one second
                                            Thread.sleep(300);
                                        } catch (InterruptedException ex) {
                                            ex.printStackTrace();
                                        }
                                        removeSelectedVertices();
                                        mapPanel.repaint();
                                        resetSelectedLabels();
                                        checkWinCondition();
                                    }
                                    // Reset the selected labels
                                    resetSelectedLabels();
                                }
                            }
                        }
                    });
                } else {
                    // Remove mouse listener if disabled
                    for (MouseListener listener : label.getMouseListeners()) {
                        label.removeMouseListener(listener);
                    }
                }
            }
        }
    }

    // Method to draw the path between vertices
    private void drawPath() {
        Graphics g = mapPanel.getGraphics();
        mapPanel.paintComponents(g);
        Graphics2D g2d = (Graphics2D) g;
        if (mapObject.path != null && mapObject.path.size() > 1) {
            g2d.setColor(Color.RED);
            for (int i = 0; i < mapObject.path.size() - 1; i++) {
                Vertex v1 = mapObject.path.get(i);
                Vertex v2 = mapObject.path.get(i + 1);

                // Get the center coordinates of the image labels representing the vertices
                Point p1 = getCenterPoint(v1);
                Point p2 = getCenterPoint(v2);

                // Draw a line between the center points of the image labels
                g2d.draw(new Line2D.Double(p1.x, p1.y, p2.x, p2.y));
            }
        }
    }

    // Method to get the center point of the image label representing a vertex
    private Point getCenterPoint(Vertex vertex) {
        JLabel label = getLabelForVertex(vertex);
        if (label != null) {
            int x = label.getX() + label.getWidth() / 2;
            int y = label.getY() + label.getHeight() / 2;
            return new Point(x, y);
        }
        return null;
    }

    // Method to get the image label representing a vertex
    private JLabel getLabelForVertex(Vertex vertex) {
        for (Component component : mapPanel.getComponents()) {
            if (component instanceof JLabel) {
                JLabel label = (JLabel) component;
                int index = mapPanel.getComponentZOrder(label);
                int row = index / 16;
                int col = index % 16;
                //Vertex vertexForLabel = (Vertex) label.getClientProperty("vertex");
                if (vertex.row == row && vertex.col == col) {
                    return label;
                }
            }
        }
        return null;
    }


    // Method to reset the selected labels
    private void resetSelectedLabels() {
        if (selectedLabel1 != null) {
            selectedLabel1.setBorder(null);
            selectedLabel1 = null;
        }
        if (selectedLabel2 != null) {
            selectedLabel2.setBorder(null);
            selectedLabel2 = null;
        }
    }

    // Method to remove the selected vertices from the map
    private void removeSelectedVertices() {
        // Get the indices of the selected vertices
        int index1 = mapPanel.getComponentZOrder(selectedLabel1);
        int index2 = mapPanel.getComponentZOrder(selectedLabel2);

        // Calculate the row and column of each vertex based on its index
        int row1 = index1 / 16;
        int col1 = index1 % 16;
        int row2 = index2 / 16;
        int col2 = index2 % 16;

        // Set the icons of the selected labels to null (make them invisible)
        selectedLabel1.setIcon(null);
        selectedLabel2.setIcon(null);

        // Optionally, you can disable the labels to prevent further clicks
        selectedLabel1.setEnabled(false);
        selectedLabel2.setEnabled(false);

        // Set the 'enabled' property of the vertices to false
        mapObject.map[row1][col1].enabled = false;
        mapObject.map[row2][col2].enabled = false;

        mapObject.clearPath();

        remainingVertices -= 2;

    }

    private boolean canBeLinked(JLabel selectedLabel1, JLabel selectedLabel2) {
        // Get the indices of the selected vertices
        int index1 = mapPanel.getComponentZOrder(selectedLabel1);
        int index2 = mapPanel.getComponentZOrder(selectedLabel2);

        // Calculate the row and column of each vertex based on its index
        int row1 = index1 / 16;
        int col1 = index1 % 16;
        int row2 = index2 / 16;
        int col2 = index2 % 16;
        Vertex v = mapObject.map[row1][col1];
        Vertex w = mapObject.map[row2][col2];
        return v.info == w.info && mapObject.hasLegalPath(v, w);
    }

    // Method to check if the player wins
    private void checkWinCondition() {
        if (remainingVertices == 0) {
            // Stop the timer
            timer.stop();
            pauseButton.setEnabled(false);
            // Display a message indicating that the player wins
            JOptionPane.showMessageDialog(this, "Congratulations! You win!");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BasicModeGUI basicModeGUI = new BasicModeGUI();
                basicModeGUI.setVisible(true);
            }
        });
    }
}

