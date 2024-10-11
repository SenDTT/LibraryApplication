package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddBookForm extends JPanel {

    public AddBookForm() {
        // Use GridBagLayout for layout management
        setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel formPanel = new JPanel(new GridBagLayout());

        // Create the header label
        JLabel headerLabel = new JLabel("Add Book Title", JLabel.LEFT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(headerLabel, gbc);

        // Author First Name Label and Text Field
        JLabel firstNameLabel = new JLabel("Author First Name");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(firstNameLabel, gbc);

        JTextField firstNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(firstNameField, gbc);

        // Author Last Name Label and Text Field
        JLabel lastNameLabel = new JLabel("Author Last Name");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lastNameLabel, gbc);

        JTextField lastNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(lastNameField, gbc);

        // Book Title Label and Text Field
        JLabel bookTitleLabel = new JLabel("Book Title");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(bookTitleLabel, gbc);

        JTextField bookTitleField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(bookTitleField, gbc);

        // Add Book Button
        JButton addButton = new JButton("Add Book");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2; // Span across two columns to center the button
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(addButton, gbc);

        add(formPanel, BorderLayout.NORTH);
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String firstName = firstNameField.getText();
            	String lastName = lastNameField.getText();
            	String bookTitleString = bookTitleField.getText();
            	if (firstName.equals("") || lastName.equals("") || bookTitleString.equals("")) {
            		JOptionPane.showMessageDialog(
                			AddBookForm.this,
                            "Please fill all data!!!",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE
                    );
            	} else {
            		Data.addBook(bookTitleString);
            		JOptionPane.showMessageDialog(
                			AddBookForm.this,
                            "Add book Successfull!!!",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE
                    );
            		firstNameField.setText("");
                	lastNameField.setText("");
                	bookTitleField.setText("");
            	}
            }
        });
    }
}

