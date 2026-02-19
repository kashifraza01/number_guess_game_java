package numberguessgame;

import javax.swing.*; // GUI components jaise JButton, JLabel, JTextField
import java.awt.*; // GUI design ke liye layout aur colors
import java.awt.event.*; // Button click waghera handle karne ke liye
import java.io.*; // File read/write ke liye
import java.util.Random; // Random number generate karne ke liye

public class NumberGuessGame extends JFrame implements ActionListener {

    // GUI components declare kiye ja rahe hain
    private final JTextField inputfield;
    private final JButton guessbutton, resetbutton;
    private final JLabel messagelabel, highscorelabel;

    // Game-related variables
    private int randomnumber; // Random number jo guess karna hai
    private int attemptsleft; // Kitne guesses remaining hain
    private int attemptsused; // Kitne guesses use ho chuke hain
    private int highscore = Integer.MAX_VALUE; // Best score ab tak

    private static final int max_attempts = 10; // Maximum 10 attempts allowed
    private static final String highscore_file = "highscore.txt"; // Highscore store karne ka file

    // Constructor — Game GUI create karta hai
    public NumberGuessGame() {
        setTitle("🎯 Number Guessing Game"); // Window ka title
        setSize(600, 400); // Window ka size
        setDefaultCloseOperation(EXIT_ON_CLOSE); // Close button ka behavior
        setLocationRelativeTo(null); // Screen ke center mein show hoga
        setResizable(false); // Resize allow nahi

        // Background panel set kiya gaya hai with custom image
        backgroundpanel panel = new backgroundpanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Components vertically arrange honge
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Padding add ki gayi
        add(panel); // Panel ko frame mein add kiya

        // Title label banaya gaya
        JLabel title = new JLabel("🎯 Guess a number between 1 and 100", JLabel.CENTER);
        title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 22)); // Emoji font set kiya
        title.setForeground(Color.WHITE); // Text ka color white
        title.setAlignmentX(Component.CENTER_ALIGNMENT); // Center align
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // Space add ki gayi

        // User input field banaya gaya
        inputfield = new JTextField();
        inputfield.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        inputfield.setMaximumSize(new Dimension(200, 40));
        inputfield.setHorizontalAlignment(JTextField.CENTER); // Text center mein hoga
        inputfield.setBackground(new Color(230, 230, 250)); // Light lavender background
        inputfield.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(inputfield);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // "Guess" button banaya gaya
        guessbutton = new JButton("Guess");
        guessbutton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 18));
        guessbutton.setBackground(new Color(0, 123, 255)); // Blue background
        guessbutton.setForeground(Color.WHITE); // White text
        guessbutton.setFocusPainted(false); // Button border disable
        guessbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        guessbutton.addActionListener(this); // Click action handle
        inputfield.addActionListener(this); // Enter key pe bhi kaam karega
        panel.add(guessbutton);

        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Message label jo user ko guide karega
        messagelabel = new JLabel("You have 10 attempts.", JLabel.CENTER);
        messagelabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        messagelabel.setForeground(Color.YELLOW);
        messagelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(messagelabel);

        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Highscore label
        highscorelabel = new JLabel("🏆 High Score: --", JLabel.CENTER);
        highscorelabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        highscorelabel.setForeground(new Color(144, 238, 144));
        highscorelabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(highscorelabel);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Reset button
        resetbutton = new JButton("Reset Game");
        resetbutton.setFont(new Font("Segoe UI Emoji", Font.BOLD, 16));
        resetbutton.setBackground(new Color(220, 53, 69)); // Red color
        resetbutton.setForeground(Color.WHITE);
        resetbutton.setFocusPainted(false);
        resetbutton.setEnabled(false); // Pehle disable hoga
        resetbutton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetbutton.addActionListener(e -> resetgame()); // Reset click event
        panel.add(resetbutton);

        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Highscore file se load karo
        loadhighscore();

        // Random number generate karo game ke start mein
        generaterandomnumber();

        // Show the GUI
        setVisible(true);
    }

    // Random number generate karne wali method
    private void generaterandomnumber() {
        randomnumber = new Random().nextInt(100) + 1; // 1-100 ke beech number
        attemptsleft = max_attempts; // Attempts reset
        attemptsused = 0; // Used attempts bhi reset
        messagelabel.setText("You have " + max_attempts + " attempts.");
        messagelabel.setForeground(Color.YELLOW);
    }

    // Game ko reset karne wali method
    private void resetgame() {
        generaterandomnumber(); // New number generate karo
        inputfield.setText(""); // Input field clear karo
        inputfield.setEditable(true);
        inputfield.requestFocus(); // Cursor input pe le jao
        guessbutton.setEnabled(true); // Guess button active
        resetbutton.setEnabled(false); // Reset button disable
    }

    // Highscore file se read karne wali method
    private void loadhighscore() {
        try (BufferedReader reader = new BufferedReader(new FileReader(highscore_file))) {
            String line = reader.readLine();
            if (line != null) {
                highscore = Integer.parseInt(line.trim()); // File se score read
            }
        } catch (IOException | NumberFormatException e) {
            highscore = Integer.MAX_VALUE; // Agar error aye to default
        }
        updatehighscorelabel(); // Label update karo
    }

    // Highscore ko file mein save karne wali method
    private void savehighscore() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(highscore_file))) {
            writer.write(String.valueOf(highscore));
        } catch (IOException e) {
            System.err.println("Error saving highscore: " + e.getMessage());
        }
    }

    // Highscore label update karna
    private void updatehighscorelabel() {
        if (highscore == Integer.MAX_VALUE) {
            highscorelabel.setText("🏆 High Score: --");
        } else {
            highscorelabel.setText("🏆 High Score: " + highscore + " attempts");
        }
    }

    // Jab guess button ya enter key press hoti hai to ye method chalegi
    @Override
    public void actionPerformed(ActionEvent e) {
        String userinput = inputfield.getText().trim();

        if (userinput.isEmpty()) {
            messagelabel.setText("Please enter a number.");
            messagelabel.setForeground(Color.ORANGE);
            return;
        }

        try {
            int guess = Integer.parseInt(userinput); // Input ko number mein convert karo

            if (guess < 1 || guess > 100) {
                messagelabel.setText("Enter a number between 1 and 100.");
                messagelabel.setForeground(Color.ORANGE);
                return;
            }

            attemptsleft--; // Attempt kam karo
            attemptsused++; // Used attempt barhao

            if (guess == randomnumber) {
                // Sahi number guess kiya
                messagelabel.setText("🎉 Correct! You guessed it in " + attemptsused + " attempts.");
                messagelabel.setForeground(Color.GREEN);

                // Highscore update karo agar better hai
                if (attemptsused < highscore) {
                    highscore = attemptsused;
                    savehighscore();
                    updatehighscorelabel();
                }

                inputfield.setEditable(false);
                guessbutton.setEnabled(false);
                resetbutton.setEnabled(true);

            } else {
                // Wrong guess message
                JOptionPane.showMessageDialog(this, "Wrong guess!", "Try Again", JOptionPane.WARNING_MESSAGE);

                if (guess < randomnumber) {
                    messagelabel.setText("📉 Too low! Attempts left: " + attemptsleft);
                } else {
                    messagelabel.setText("📈 Too high! Attempts left: " + attemptsleft);
                }
                messagelabel.setForeground(Color.CYAN);
            }

            // Attempts khatam hogaye
            if (attemptsleft == 0 && guess != randomnumber) {
                messagelabel.setText("😢 Out of attempts! Number was " + randomnumber);
                messagelabel.setForeground(Color.RED);
                inputfield.setEditable(false);
                guessbutton.setEnabled(false);
                resetbutton.setEnabled(true);
            }

        } catch (NumberFormatException ex) {
            // Agar number na ho to
            messagelabel.setText("Invalid input. Please enter a number.");
            messagelabel.setForeground(Color.ORANGE);
        }
    }

    // Custom background panel class
    private class backgroundpanel extends JPanel {
        private final Image backgroundimage;

        public backgroundpanel() {
            java.net.URL imgUrl = getClass().getResource("areeba.png");
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                backgroundimage = icon.getImage(); // Image load karo
            } else {
                System.err.println("Image not found");
                backgroundimage = null;
            }
        }

        // Image ko paint karne wali method
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundimage != null) {
                g.drawImage(backgroundimage, 0, 0, getWidth(), getHeight(), this);
            } else {
                g.setColor(Color.DARK_GRAY); // Fallback color
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    // Main method — Program yahan se start hota hai
    public static void main(String[] args) {
        new NumberGuessGame(); // Game start karo
    }
}
