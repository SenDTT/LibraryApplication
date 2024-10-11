package librarysystem;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.Map;
import business.*;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public class CheckoutForm extends JPanel {
    private JTextField memberIdField;
    private JTextField isbnField;
    private JButton findMemberButton;
    private JButton checkoutButton;
    private JLabel statusLabel;
    private JTable checkoutTable;
    private DefaultTableModel tableModel;
    private Map<String, LibraryMember> membersDatabase;
    private Map<String, Book> booksDatabase;

    public CheckoutForm() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input for Member ID
        JLabel memberIdLabel = new JLabel("Member ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        add(memberIdLabel, gbc);

        memberIdField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        add(memberIdField, gbc);

        // Input for ISBN
        JLabel isbnLabel = new JLabel("ISBN:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        add(isbnLabel, gbc);

        isbnField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        add(isbnField, gbc);

        // Button to find member
        findMemberButton = new JButton("Find Member and Book");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1; // Corrected from 2 to 1
        gbc.weightx = 0;
        add(findMemberButton, gbc);

        // Button to checkout the book
        checkoutButton = new JButton("Checkout Book");
        checkoutButton.setEnabled(false);
        gbc.gridx = 2;
        gbc.gridy = 1;
        add(checkoutButton, gbc);

        // Label to display status
        statusLabel = new JLabel("Status: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3; // Span across all three columns
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(statusLabel, gbc);

        // Table to display checkout records
        String[] columnNames = {"Member", "Book ISBN", "Title", "Checkout Date", "Due Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table read-only
            }
        };
        checkoutTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(checkoutTable);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0; // Allow table to expand vertically
        add(tableScrollPane, gbc);

        // Action listeners
        findMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = memberIdField.getText().trim();
                String isbn = isbnField.getText().trim();
                findMemberAndBook(memberId, isbn);
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = memberIdField.getText().trim();
                String isbn = isbnField.getText().trim();
                DataAccess da = new DataAccessFacade();
                CheckoutEntry resCheckoutEntry = da.checkoutBook(memberId, isbn);
                tableModel.addRow(new Object[]{
                		memberId,
                		isbn, 
                		resCheckoutEntry.getBookCopy().getBook().getTitle(), 
                		resCheckoutEntry.getCheckedOutDate(), 
                		resCheckoutEntry.getDueDate()}
                );
                JOptionPane.showMessageDialog(CheckoutForm.this, "Book checked out successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                memberIdField.setText("");
                isbnField.setText("");
                checkoutButton.setEnabled(false);
                statusLabel.setText("Status: ");
            }
        });
    }

    private void findMemberAndBook(String memberId, String isbn) {
    	DataAccess da = new DataAccessFacade();
    	this.membersDatabase = da.readMemberMap();
        this.booksDatabase = da.readBooksMap();
        LibraryMember member = membersDatabase.get(memberId);
        Book book = booksDatabase.get(isbn);

        if (book == null) {
        	tableModel.setRowCount(0);
//            JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Status: Book not found.");
            checkoutButton.setEnabled(false);
            return;
        }
        
        loadCheckoutRecordsForBook(isbn);
        
        if (member == null) {
//            JOptionPane.showMessageDialog(this, "Library Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Status: Member not found.");
            checkoutButton.setEnabled(false);
            return;
        }

        if (!book.isAvailable()) {
//            JOptionPane.showMessageDialog(this, "No copies of the book are available for checkout.", "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Status: Book not available.");
            checkoutButton.setEnabled(false);
        } else {
            statusLabel.setText("Status: Book is available for checkout.");
            checkoutButton.setEnabled(true);
        } 
    }
    
    private void loadCheckoutRecordsForBook(String isbn) {
        tableModel.setRowCount(0);
        DataAccess da = new DataAccessFacade();
        for (LibraryMember member :  da.readMemberMap().values()) {
	        for (CheckoutEntry entry : member.getCheckoutEntries()) {
		        if (entry.getBookCopy().getBook().getIsbn().equals(isbn)) {
		            String bookTitle = entry.getBookCopy().getBook().getTitle();
		            LocalDate checkoutDate = entry.getCheckedOutDate();
		            LocalDate dueDate = entry.getDueDate();
		            tableModel.addRow(new Object[]{member.getMemberId(), isbn, bookTitle, checkoutDate, dueDate});
	        	}
	        }
        }
    }
}
