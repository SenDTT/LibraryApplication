package librarysystem;

import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import business.Book;
import business.ControllerInterface;
import business.SystemController;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public class AddBookCopyForm extends JPanel {
	// bg_add_book_copy.jpg
    private BufferedImage backgroundImage;
	private String title = "Add Book Copy";
	private HashMap<String, Book> allBooks;
	private JComboBox<String> bookList;
	private JTextField quantityField;
	private JButton addButton;
    private DefaultComboBoxModel<String> bookListModel;
	DataAccess da = new DataAccessFacade();
    ControllerInterface ci = new SystemController();
    private Map<String, Integer> bookQuantities = new HashMap<>();
    private JLabel currentQuantityLabel;
	
	public AddBookCopyForm() {
		try {
	        String currDirectory = System.getProperty("user.dir");
	        backgroundImage = ImageIO.read(new File(currDirectory + "/src/images/bg_add_book_copy.jpg"));
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

		// Use BorderLayout for the main layout
		setLayout(new BorderLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); // Padding around components
		gbc.fill = GridBagConstraints.BOTH; // Make components fill the space
		JPanel formPanel = new JPanel(new GridBagLayout());
		formPanel.setOpaque(false);

		// Create the header label
		JLabel headerLabel = new JLabel("Add Book Copy", JLabel.LEFT);
		headerLabel.setFont(new Font("Arial", Font.BOLD, 16));
		headerLabel.setForeground(Color.BLUE);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2; // Span across two columns
		gbc.anchor = GridBagConstraints.WEST;
		formPanel.add(headerLabel, gbc);
		
		// Add the quantity label and text field
		JLabel bookLabel = new JLabel("Book:");
		bookLabel.setFont(new Font("Arial", Font.BOLD, 13));
//		bookLabel.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.NONE;
		formPanel.add(bookLabel, gbc);

		// Create and add the JList for books
		bookListModel = new DefaultComboBoxModel<>();
		loadBooks(); // Method to load books into the bookListModel
		//bookList = new JList<>(bookListModel);
		bookList = new JComboBox<>(bookListModel);

		//bookList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Add the JList to a JScrollPane
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		formPanel.add(bookList, gbc);
		
		Integer qty = bookQuantities.getOrDefault(bookListModel.getElementAt(0), 0);
		currentQuantityLabel = new JLabel("Current Quantity: " + qty);
		currentQuantityLabel.setFont(new Font("Arial", Font.BOLD, 13));
//		currentQuantityLabel.setForeground(Color.WHITE);
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.NONE;
		formPanel.add(currentQuantityLabel, gbc);

		// Add the quantity label and text field
		JLabel qtyLabel = new JLabel("Quantity:");
		qtyLabel.setFont(new Font("Arial", Font.BOLD, 13));
//		qtyLabel.setForeground(Color.WHITE);
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.NONE;
		formPanel.add(qtyLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		quantityField = new JTextField(10); // Set preferred size for text field
		formPanel.add(quantityField, gbc);

		// Add the Add button
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		addButton = new JButton("Add");
		formPanel.add(addButton, gbc);

		// Add formPanel to the right side of the main frame
		add(formPanel);
		
		bookList.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	            String selectedBook = (String) bookList.getSelectedItem();
	            Integer currentQuantity = bookQuantities.getOrDefault(selectedBook, 0); // Default to 0 if not found
	            currentQuantityLabel.setText("Current Quantity: " + currentQuantity);
	        }
	    });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBook = (String) bookList.getSelectedItem();
                String quantityText = quantityField.getText().trim();
                String isbn = selectedBook.substring(0, selectedBook.indexOf(" - "));

                // Check if the quantity is a number
                if (quantityText.equals("") || !Util.isNumeric(quantityText)) {
                    JOptionPane.showMessageDialog(null, 
                        "Please enter a valid number for quantity.", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE);
                    return; // Stop the process if validation fails
                }
                
                Book book = allBooks.get(isbn);

                boolean result = ci.addBookCopies(book, Integer.parseInt(quantityText));
                
                if (result) {
                	// success
                	JOptionPane.showMessageDialog(
                			AddBookCopyForm.this,
                            "Successful!!!",
                            title,
                            JOptionPane.INFORMATION_MESSAGE
                    );
                	loadBooks();
                	quantityField.setText("");
                	bookList.setSelectedIndex(0);
                	String defaultBook = (String) bookList.getSelectedItem();
                	Integer quantity = bookQuantities.getOrDefault(defaultBook, 0);
                	currentQuantityLabel.setText("Current Quantity: " + quantity);
                } else {
                	// failed
                	JOptionPane.showMessageDialog(
                			AddBookCopyForm.this,
                            "Add Fail, Please try again!!!",
                            title,
                            JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });
	}
	
	private void loadBooks() {
		this.bookListModel = new DefaultComboBoxModel<String>();
		this.allBooks = da.readBooksMap();
		this.allBooks.forEach((isbn, book) -> {
			String title = isbn + " - " + book.getTitle();
			bookListModel.addElement(title);
			
			bookQuantities.put(title, book.getNumCopies());
		});
	}
	
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
//            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
