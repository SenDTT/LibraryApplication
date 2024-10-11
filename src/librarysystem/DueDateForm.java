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
import dataaccess.User;

public class DueDateForm extends JPanel{
    private JTextField memberIdField;
    private JTextField isbnField;
    private JButton findMemberButton;
    private JButton checkoutButton;
    private JLabel statusLabel;
    private JTable checkoutTable;
    private DefaultTableModel tableModel;
    private Map<String, LibraryMember> membersDatabase;
    private Map<String, Book> booksDatabase;
    private DataCallback callback;

    public DueDateForm(DataCallback callback) {
    	this.callback = callback;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input for ISBN
        JLabel isbnLabel = new JLabel("ISBN:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        add(isbnLabel, gbc);

        isbnField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        add(isbnField, gbc);

        // Button to find member
        findMemberButton = new JButton("Find OverDue Book");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1; // Corrected from 2 to 1
        gbc.weightx = 0;
        add(findMemberButton, gbc);

        // Label to display status
        statusLabel = new JLabel("Status: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3; // Span across all three columns
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(statusLabel, gbc);

        // Table to display checkout records
        String[] columnNames = {"Copy ID", "Member", "Book ISBN", "Title", "Checkout Date", "Due Date", "Librarian"};
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
                String isbn = isbnField.getText().trim();
                findMemberAndBook(isbn);
            }
        });

    }

    private void findMemberAndBook(String isbn) {
    	if (isbn.isEmpty()) {
    		 statusLabel.setText("Status: Please input Book ISBN!!!");
             checkoutButton.setEnabled(false);
             return;
    	}
    	DataAccess da = new DataAccessFacade();
    	this.membersDatabase = da.readMemberMap();
        this.booksDatabase = da.readBooksMap();
        Book book = booksDatabase.get(isbn);

        if (book == null) {
        	tableModel.setRowCount(0);
//            JOptionPane.showMessageDialog(this, "Book not found!", "Error", JOptionPane.ERROR_MESSAGE);
            statusLabel.setText("Status: Book not found.");
            return;
        }
        
        loadCheckoutRecordsForBook(isbn);
    }
    
    private void loadCheckoutRecordsForBook(String isbn) {
    	boolean flag = false;
        tableModel.setRowCount(0);
        DataAccess da = new DataAccessFacade();
        for (LibraryMember member :  da.readMemberMap().values()) {
	        for (CheckoutEntry entry : member.getCheckoutEntries()) {
		        if (	entry.getBookCopy().getBook().getIsbn().equals(isbn) && 
		        		entry.getBookCopy().isAvailable() == false && 
		        		entry.getDueDate().isBefore(LocalDate.now())
		        	) {
		        	flag = true;
		            String bookTitle = entry.getBookCopy().getBook().getTitle();
		            int copyNum = entry.getBookCopy().getCopyNum();
		            LocalDate checkoutDate = entry.getCheckedOutDate();
		            LocalDate dueDate = entry.getDueDate();
		            User user = entry.getUser();
		            tableModel.addRow(new Object[]{copyNum, member.getMemberId(), isbn, bookTitle, checkoutDate, dueDate, user.getId()});
	        	}
	        }
        }
        if (!flag) {
        	statusLabel.setText("Status: Doesn't have OverDue!!!");
            return;
        }
    }
}
