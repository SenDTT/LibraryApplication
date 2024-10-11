package librarysystem;

import javax.imageio.ImageIO;
import javax.swing.*;

import dataaccess.*;
import dataaccess.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginForm extends JPanel {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private DataCallback callback;
    private BufferedImage backgroundImage;

    public LoginForm(DataCallback callback) {
        try {
            String currDirectory = System.getProperty("user.dir");
            backgroundImage = ImageIO.read(new File(currDirectory + "/src/librarysystem/library.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.callback = callback;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(600, 300));

        // Create a JLayeredPane to hold both the background image and the form
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(600, 300));

        // Create the form panel with GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false); // Make the panel transparent so the background is visible
        formPanel.setBounds(150, 50, 300, 150); // Adjust the size and position for a smaller form

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(3, 5, 3, 5); // Reduce insets for a tighter layout

        // Header
        JLabel headerLabel = new JLabel("Login", JLabel.LEFT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setForeground(Color.ORANGE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 10, 5);
        formPanel.add(headerLabel, gbc);

        // Username Label
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setForeground(Color.white);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 3, 5);
        formPanel.add(usernameLabel, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.white);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(0, 5, 3, 5);
        formPanel.add(passwordLabel, gbc);

        // Username TextField
        usernameField = new JTextField(15); // Reduce the columns for a smaller input field
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 3, 5);
        formPanel.add(usernameField, gbc);

        // Password TextField
        passwordField = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(5, 5, 5, 5);
        formPanel.add(passwordField, gbc);

        // Login Button
        loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(loginButton, gbc);

        // Add formPanel to the layeredPane at a higher layer
        layeredPane.add(formPanel, JLayeredPane.PALETTE_LAYER);

        // Add the layeredPane to the main panel
        add(layeredPane, BorderLayout.CENTER);

        // Button ActionListener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());
                DataAccess da = new DataAccessFacade();
                Map<String, User> users = da.readUserMap();
                boolean isSuccess = false;
                for (User user : users.values()) {
                    if (username.equals(user.getId()) && password.equals(user.getPassword())) {
                        isSuccess = true;
                        switch (user.getAuthorization()) {
                            case LIBRARIAN:
                                boolean[] enabledSeller = {true, false, true, false, false};
                                callback.onLoginSuccess(enabledSeller);
                                break;
                            case ADMIN:
                                boolean[] enabledMember = {true, true, true, true, true};
                                callback.onLoginSuccess(enabledMember);
                                break;
                            case BOTH:
                                boolean[] enabledBoth = {true, true, true, true, true};
                                callback.onLoginSuccess(enabledBoth);
                                break;
                        }
                        JOptionPane.showMessageDialog(
                                LoginForm.this,
                                "Login Successful!!!",
                                "Login Info",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }
                }
                if (!isSuccess) {
                    JOptionPane.showMessageDialog(
                            LoginForm.this,
                            "Login Fail, Please try again!!!",
                            "Login Info",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
