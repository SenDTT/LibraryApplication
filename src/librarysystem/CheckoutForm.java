package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import business.*;
import dataaccess.DataAccess;
import dataaccess.DataAccessFacade;

public class CheckoutForm extends JPanel {
    private JTextField memberIdField;
    private JButton findMemberButton;
    private JComboBox<String> availableBooksComboBox;
    private JButton checkoutButton;
    private JLabel statusLabel;
    private Map<String, LibraryMember> membersDatabase;
    private Map<String, Book> booksDatabase;

    public CheckoutForm() {
    	DataAccess da = new DataAccessFacade();
    	this.membersDatabase = da.readMemberMap();
        this.booksDatabase = da.readBooksMap();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Input for Member ID
        JLabel memberIdLabel = new JLabel("Member ID:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(memberIdLabel, gbc);

        memberIdField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 0;
        add(memberIdField, gbc);

        // Button to find member
        findMemberButton = new JButton("Find Library Member");
        gbc.gridx = 2;
        gbc.gridy = 0;
        add(findMemberButton, gbc);

        // ComboBox for available books
        availableBooksComboBox = new JComboBox<>();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        add(availableBooksComboBox, gbc);

        // Button to checkout the selected book
        checkoutButton = new JButton("Checkout Book");
        gbc.gridx = 2;
        gbc.gridy = 1;
        add(checkoutButton, gbc);

        // Label to display status
        statusLabel = new JLabel("Status: ");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        add(statusLabel, gbc);

        // Action listeners
        findMemberButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String memberId = memberIdField.getText().trim();
                findMember(memberId);
            }
        });

        checkoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedBookId = (String) availableBooksComboBox.getSelectedItem();
                if (selectedBookId != null && !selectedBookId.isEmpty()) {
                    checkoutBook(selectedBookId);
                }
            }
        });
    }

    private void findMember(String memberId) {
        LibraryMember member = membersDatabase.get(memberId);
        availableBooksComboBox.removeAllItems();

        if (member != null) {
            // Populate available books
            for (Book book : booksDatabase.values()) {
                if (book.isAvailable()) {
                    availableBooksComboBox.addItem(book.getIsbn() + " - " + book.getTitle());
                }
            }

            availableBooksComboBox.setEnabled(true);
            checkoutButton.setEnabled(true);
            statusLabel.setText("Status: Select a book to checkout.");
        } else {
            JOptionPane.showMessageDialog(this, "Library Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
            availableBooksComboBox.setEnabled(false);
            checkoutButton.setEnabled(false);
        }
    }

    private void checkoutBook(String selectedBookId) {
        String memberId = memberIdField.getText().trim();
        LibraryMember member = membersDatabase.get(memberId);

        if (member != null) {
            // Tìm sách dựa trên ID
            Book bookToCheckout = booksDatabase.get(selectedBookId.split(" - ")[0]);

            if (bookToCheckout != null && bookToCheckout.isAvailable()) {
            	DataAccess da = new DataAccessFacade();
                // Cập nhật trạng thái sách
                //bookToCheckout.setAvailable(false);
                //member.addBook(bookToCheckout);
            	
            	JOptionPane.showMessageDialog(this, "Book checked out successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            	memberIdField.setText("");
            	availableBooksComboBox.removeAllItems();
            	availableBooksComboBox.setEnabled(false);
                checkoutButton.setEnabled(false);
                statusLabel.setText("Status: Select a book to checkout.");
            } else {
                JOptionPane.showMessageDialog(this, "Selected book is not available.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Library Member not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
//
//    public static void main(String[] args) {
//        // Giả sử có một số dữ liệu thành viên và sách có sẵn
//        Map<String, LibraryMember> membersDatabase = new HashMap<>();
//        LibraryMember member = new LibraryMember("M001", "John Doe");
//        membersDatabase.put("M001", member);
//
//        Map<String, Book> booksDatabase = new HashMap<>();
//        booksDatabase.put("B001", new Book("B001", "Java Programming", true));
//        booksDatabase.put("B002", new Book("B002", "Design Patterns", true));
//
//        JFrame frame = new JFrame("Library Checkout");
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setSize(600, 300);
//        frame.add(new CheckoutForm(membersDatabase, booksDatabase));
//        frame.setVisible(true);
//    }
}
