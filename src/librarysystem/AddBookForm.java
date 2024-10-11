package librarysystem;

import javax.swing.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import business.Author;
import business.ControllerInterface;
import business.SystemController;

import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddBookForm extends JPanel {
	DataAccess da = new DataAccessFacade();
	ControllerInterface ci = new SystemController();
	private List<Author> authors = new ArrayList<Author>();
	private HashMap<String, Author> allAuthors;
	private JComboBox<String> authorList;
	private JComboBox<String> daysList;
	private DefaultComboBoxModel<String> authorListModel;
	private DefaultComboBoxModel<String> dueDaysListModel;
	private DefaultListModel<String> listModel = new DefaultListModel<String>();
	private JList<String> mainList;
	private JScrollPane mainScroll;
	private JButton removeSelectedButton;

	public void loadAuthors() {
		this.authorListModel = new DefaultComboBoxModel<String>();
		this.allAuthors = da.readAuthorMap();
		this.allAuthors.forEach((authorId, author) -> {
			String title = authorId + " - " + author.getFirstName() + " " + author.getLastName();
			authorListModel.addElement(title);
		});
	}

	public AddBookForm() {
		loadAuthors();

		// Use GridBagLayout for layout management
		setLayout(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // Padding around components
		gbc.fill = GridBagConstraints.HORIZONTAL;
		JPanel formPanel = new JPanel(new GridBagLayout());

		// Create the header label
		JLabel headerLabel = new JLabel("Add Book", JLabel.LEFT);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		headerLabel.setForeground(Color.BLUE);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2; // Span across two columns
		gbc.anchor = GridBagConstraints.WEST;
		formPanel.add(headerLabel, gbc);

		// Author First Name Label and Text Field
		JLabel isbnLabel = new JLabel("ISBN:");
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		formPanel.add(isbnLabel, gbc);

		JTextField isbnField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 1;
		formPanel.add(isbnField, gbc);

		// Author Last Name Label and Text Field
		JLabel bookTitleLabel = new JLabel("Book Title:");
		gbc.gridx = 0;
		gbc.gridy = 2;
		formPanel.add(bookTitleLabel, gbc);

		JTextField bookTitleField = new JTextField(15);
		gbc.gridx = 1;
		gbc.gridy = 2;
		formPanel.add(bookTitleField, gbc);

		// Book Title Label and Text Field
		JLabel checkoutLengthLabel = new JLabel("Max Checkout Length (days):");
		gbc.gridx = 0;
		gbc.gridy = 3;
		formPanel.add(checkoutLengthLabel, gbc);

		// dropdown select length days
		dueDaysListModel = new DefaultComboBoxModel<>();
		dueDaysListModel.addElement("21");
		dueDaysListModel.addElement("7");
		daysList = new JComboBox<>(dueDaysListModel);

		// Add the JList to a JScrollPane
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.BOTH;
		formPanel.add(daysList, gbc);

		// Add the quantity label and text field
		JLabel bookLabel = new JLabel("Authors:");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		formPanel.add(bookLabel, gbc);

		mainList = createJList();
		mainList.setFixedCellWidth(70);
		mainScroll = new JScrollPane(mainList);

        gbc.gridx = 1;
        gbc.gridy = 4;
		gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0; // Allow table to expand vertically
        formPanel.add(mainScroll, gbc);
        
		//remove item components
        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.NONE;
		removeSelectedButton = new JButton("Remove Selected");
		formPanel.add(removeSelectedButton, gbc);

		// dropdown select authors
		authorListModel = new DefaultComboBoxModel<>();
		loadAuthors();
		authorList = new JComboBox<>(authorListModel);

		// Add the JList to a JScrollPane
		gbc.gridx = 1;
		gbc.gridy = 6;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		formPanel.add(authorList, gbc);

		gbc.gridx = 2;
		gbc.gridy = 6;
		gbc.fill = GridBagConstraints.NONE;
		JButton addAuthor = new JButton("Add Author");
		formPanel.add(addAuthor, gbc);

		// Add Book Button
		JButton addButton = new JButton("Add Book");
		gbc.gridx = 1;
		gbc.gridy = 7;
		gbc.gridwidth = 1; // Span across two columns to center the button
		gbc.anchor = GridBagConstraints.CENTER;
		formPanel.add(addButton, gbc);
        
		add(formPanel, BorderLayout.NORTH);
		
		removeSelectedButton.addActionListener(new RemoveListener());

		addAuthor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String selectedAuthor = (String) authorList.getSelectedItem();
				String authorId = selectedAuthor.substring(0, selectedAuthor.indexOf(" - "));
				Author author = allAuthors.get(authorId);
				authors.add(author);
				listModel.addElement(selectedAuthor);
			}

		});

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String isbn = isbnField.getText().trim();
				String checkoutLength = (String) daysList.getSelectedItem();
				String bookTitleString = bookTitleField.getText().trim();

				if (authors.size() == 0 || isbn.equals("") || checkoutLength.equals("") || bookTitleString.equals("")) {
					JOptionPane.showMessageDialog(AddBookForm.this, "Please fill all data!!!", "Info",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				// Check if the quantity is a number
				if (!Util.isNumeric(checkoutLength)) {
					JOptionPane.showMessageDialog(null, "Please enter a valid number for quantity.", "Invalid Input",
							JOptionPane.ERROR_MESSAGE);
					return; // Stop the process if validation fails
				}
				int maxLength = Integer.parseInt(checkoutLength);

				ci.addNewBook(isbn, bookTitleString, maxLength, authors);
				JOptionPane.showMessageDialog(AddBookForm.this, "Add book Successfull!!!", "Info",
						JOptionPane.INFORMATION_MESSAGE);
				isbnField.setText("");
				daysList.setSelectedIndex(0);
				bookTitleField.setText("");
				listModel.removeAllElements();
			}
		});
	}
	
	private JList<String> createJList() {
		JList<String> ret = new JList<String>(listModel);
		ret.setVisibleRowCount(4);
		return ret;
	}
	
	class RemoveListener implements ActionListener {
		public void actionPerformed(ActionEvent evt){
			List<String> selected = mainList.getSelectedValuesList();
			for(String s: selected) {
				listModel.removeElement(s);
				String authorId = s.substring(0, s.indexOf(" - "));
				Author author = allAuthors.get(authorId);
				authors.remove(author);
			}
		}
	}
}
