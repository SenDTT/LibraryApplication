package librarysystem;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import business.Address;
import business.Author;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public class AddAuthorForm extends JPanel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6550602971964258890L;

	public AddAuthorForm() {
        setLayout(new BorderLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Padding around components
        gbc.fill = GridBagConstraints.HORIZONTAL;
        JPanel formPanel = new JPanel(new GridBagLayout());

        // Create the header label
        JLabel headerLabel = new JLabel("Add Author", JLabel.LEFT);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        headerLabel.setForeground(Color.BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // Span across two columns
        gbc.anchor = GridBagConstraints.WEST;
        formPanel.add(headerLabel, gbc);

        // Author First Name Label and Text Field
        JLabel firstNameLabel = new JLabel("First Name");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(firstNameLabel, gbc);

        JTextField firstNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        formPanel.add(firstNameField, gbc);

        // Author Last Name Label and Text Field
        JLabel lastNameLabel = new JLabel("Last Name");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(lastNameLabel, gbc);

        JTextField lastNameField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 2;
        formPanel.add(lastNameField, gbc);

        // Phone number Label and Text Field
        JLabel phoneNumberLabel = new JLabel("Phone Number");
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(phoneNumberLabel, gbc);

        JTextField phoneNumberField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(phoneNumberField, gbc);
        
        // Bio Label and Text Field
        JLabel bioLabel = new JLabel("Bio");
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(bioLabel, gbc);

        JTextField bioField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 4;
        formPanel.add(bioField, gbc);
        
        // Address Label and Text Field
        JLabel addressLabel = new JLabel("Address");
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(addressLabel, gbc);

        JLabel streetLabel = new JLabel("Street");
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(streetLabel, gbc);
        
        JTextField streetField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 6;
        formPanel.add(streetField, gbc);

        JLabel cityLabel = new JLabel("City");
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(cityLabel, gbc);
        
        JTextField cityField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 7;
        formPanel.add(cityField, gbc);

        JLabel stateLabel = new JLabel("State");
        gbc.gridx = 0;
        gbc.gridy = 8;
        formPanel.add(stateLabel, gbc);
        
        JTextField stateField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 8;
        formPanel.add(stateField, gbc);

        JLabel zipLabel = new JLabel("Zip");
        gbc.gridx = 0;
        gbc.gridy = 9;
        formPanel.add(zipLabel, gbc);
        
        JTextField zipField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 9;
        formPanel.add(zipField, gbc);
        
        // Add Book Button
        JButton addButton = new JButton("Add Author");
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2; // Span across two columns to center the button
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(addButton, gbc);

        add(formPanel, BorderLayout.NORTH);
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String firstName = firstNameField.getText().trim();
            	String lastName = lastNameField.getText().trim();
            	String bioString = bioField.getText().trim();
            	String phoneNumberString = phoneNumberField.getText().trim();
            	String streetString = streetField.getText().trim();
            	String cityString = cityField.getText().trim();
            	String stateString = stateField.getText().trim();
            	String zipString = zipField.getText().trim();
            	
            	if (firstName.equals("") || lastName.equals("") 
            						|| phoneNumberString.equals("") 
            						|| bioString.equals("")
            						|| streetString.equals("")
            						|| cityString.equals("")
            						|| stateString.equals("")
            						|| zipString.equals("")
            						) {
            		JOptionPane.showMessageDialog(
                			AddAuthorForm.this,
                            "Please fill all data!!!",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE
                    );
            	} else {
            		
            		Address address = new Address(
            				streetString, 
            				cityString, 
            				phoneNumberString, 
            				streetString);
            		Author author = new Author(
            				Util.getAlphaNumericString(6), 
            				firstName, 
            				lastName, 
            				phoneNumberString, 
            				address,
            				bioString);
            		
            		DataAccess da = new DataAccessFacade();
            		da.saveNewAuthor(author);
            		JOptionPane.showMessageDialog(
                			AddAuthorForm.this,
                            "Add Author Successfull!!!",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE
                    );
            		
            		firstNameField.setText("");
                	lastNameField.setText("");
                	phoneNumberField.setText("");
                	streetField.setText("");
                	cityField.setText("");
                	stateField.setText("");
                	zipField.setText("");
                	bioField.setText("");
            	}
            }
        });
    }
}