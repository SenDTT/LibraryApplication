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

public class PrintCheckoutRecordForm extends JPanel{
    private JTextField memberIdField;
    private JTextField isbnField;
    private JButton findMemberButton;
    private JButton printButton;
    private JLabel statusLabel;
    private JTable checkoutTable;
    private DefaultTableModel tableModel;
    private Map<String, LibraryMember> membersDatabase;
    private Map<String, Book> booksDatabase;
    private DataCallback callback;

    public PrintCheckoutRecordForm(DataCallback callback) {
    	this.callback = callback;
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

        // Button to find member
        findMemberButton = new JButton("Find Member");
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 1; // Corrected from 2 to 1
        gbc.weightx = 0;
        add(findMemberButton, gbc);

        // Button to checkout the book
        printButton = new JButton("Print Checkout Record");
        printButton.setEnabled(false);
        gbc.gridx = 2;
        gbc.gridy = 1;
        add(printButton, gbc);

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
                String memberId = memberIdField.getText().trim();
                findMember(memberId);
            }
        });

        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = memberIdField.getText().trim();
                DataAccess da = new DataAccessFacade();
                int i = 1;
                for (LibraryMember member :  da.readMemberMap().values()) {
                	if (member.getMemberId().equals(memberId)) {
                		for (CheckoutEntry entry : member.getCheckoutEntries()) {
            	            String bookTitle = entry.getBookCopy().getBook().getTitle();
            	            int copyNum = entry.getBookCopy().getCopyNum();
            	            LocalDate checkoutDate = entry.getCheckedOutDate();
            	            LocalDate dueDate = entry.getDueDate();
            	            User user = entry.getUser();
            	            String isbn = entry.getBookCopy().getBook().getIsbn();
            	            System.out.printf("\n\nCheckout No: %d \n", i);
            	            System.out.printf("Copy Num: %s \nMember ID: %s \nISBN: %s \nBook Title: %s \nCheckout Date: %s \nDue Date: %s \nLibrarian ID: %s \n", copyNum, member.getMemberId(), isbn, bookTitle, checkoutDate, dueDate, user.getId());
            	            i++;
            	        }
                	}
                }
                statusLabel.setText("Status: Print checkout record successful!!!");
            }
        });
    }

    private void findMember(String memberId) {
    	if (memberId.isEmpty()) {
    		 statusLabel.setText("Status: Please input Member ID!!!");
    		 printButton.setEnabled(false);
             return;
    	}
    	DataAccess da = new DataAccessFacade();
    	this.membersDatabase = da.readMemberMap();
        this.booksDatabase = da.readBooksMap();
        LibraryMember member = membersDatabase.get(memberId);
        
        if (member == null) {
            statusLabel.setText("Status: Member not found.");
            printButton.setEnabled(false);
            return;
        }
        loadCheckoutRecordsForBook(memberId);
    }
    
    private void loadCheckoutRecordsForBook(String memberId) {
    	boolean flag = false;
    	int count = 0;
        tableModel.setRowCount(0);
        DataAccess da = new DataAccessFacade();
        for (LibraryMember member :  da.readMemberMap().values()) {
	        for (CheckoutEntry entry : member.getCheckoutEntries()) {
	        	flag = true;
	        	count++;
	            String bookTitle = entry.getBookCopy().getBook().getTitle();
	            int copyNum = entry.getBookCopy().getCopyNum();
	            LocalDate checkoutDate = entry.getCheckedOutDate();
	            LocalDate dueDate = entry.getDueDate();
	            User user = entry.getUser();
	            String isbn = entry.getBookCopy().getBook().getIsbn();
	            tableModel.addRow(new Object[]{copyNum, member.getMemberId(), isbn, bookTitle, checkoutDate, dueDate, user.getId()});
	        }
        }
        if (!flag) {
        	statusLabel.setText("Status: Checkout record not found!!!");
        	printButton.setEnabled(false);
        } else {
        	statusLabel.setText("Status: Found "+ count +" checkout entries!!!");
        	printButton.setEnabled(true);
        }
    }
}
