package librarysystem;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.DefaultTableModel;

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
    private DefaultComboBoxModel<String> authorListModel;
    private DefaultTableModel tableModel;
    private JTable authorTable;
    
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

        JTextField checkoutLengthField = new JTextField(15);
        gbc.gridx = 1;
        gbc.gridy = 3;
        formPanel.add(checkoutLengthField, gbc);

		// Add the quantity label and text field
		JLabel bookLabel = new JLabel("Book:");
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		formPanel.add(bookLabel, gbc);

		authorListModel = new DefaultComboBoxModel<>();
		loadAuthors(); 
		authorList = new JComboBox<>(authorListModel);

		// Add the JList to a JScrollPane
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 1;
		gbc.fill = GridBagConstraints.BOTH;
		formPanel.add(authorList, gbc);

		gbc.gridx = 2;
		gbc.gridy = 4;
		gbc.fill = GridBagConstraints.NONE;
		JButton addAuthor = new JButton("Add Author");
		formPanel.add(addAuthor, gbc);

        // Add Book Button
        JButton addButton = new JButton("Add Book");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Span across two columns to center the button
        gbc.anchor = GridBagConstraints.CENTER;
        formPanel.add(addButton, gbc);
        
//        String[] columnNames = {"Author ID", "First Name", "LastName", "Action"};
//        tableModel = new DefaultTableModel(columnNames, 0) {
//            @Override
//            public boolean isCellEditable(int row, int column) {
//                return false; // Make table read-only
//            }
//        };
//        authorTable = new JTable(tableModel);
//
//        // Add button renderer and editor
//        authorTable.getColumnModel().getColumn(3).setCellRenderer(new ButtonRenderer());
//        authorTable.getColumnModel().getColumn(3).setCellEditor(new ButtonEditor(new JCheckBox(), authorTable));
//        
//        JScrollPane tableScrollPane = new JScrollPane(authorTable);
//        gbc.gridx = 0;
//        gbc.gridy = 3;
//        gbc.gridwidth = 3;
//        gbc.fill = GridBagConstraints.BOTH;
//        gbc.weighty = 1.0; // Allow table to expand vertically
//        add(tableScrollPane, gbc);

        add(formPanel, BorderLayout.NORTH);
        
        addAuthor.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String selectedAuthor = (String) authorList.getSelectedItem();
                String authorId = selectedAuthor.substring(0, selectedAuthor.indexOf(" - "));
                Author author = allAuthors.get(authorId);
                authors.add(author);

//                addRowToTable(author);
			}
        	
        });
        
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String isbn = isbnField.getText().trim();
            	String checkoutLength = checkoutLengthField.getText().trim();
            	String bookTitleString = bookTitleField.getText().trim();
            	
            	if (authors.size() == 0 || isbn.equals("") || checkoutLength.equals("") || bookTitleString.equals("")) {
            		JOptionPane.showMessageDialog(
                			AddBookForm.this,
                            "Please fill all data!!!",
                            "Info",
                            JOptionPane.INFORMATION_MESSAGE
                    );
            		return;
            	}
            	
                // Check if the quantity is a number
                if (!Util.isNumeric(checkoutLength)) {
                    JOptionPane.showMessageDialog(null, 
                        "Please enter a valid number for quantity.", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE);
                    return; // Stop the process if validation fails
                }
                int maxLength = Integer.parseInt(checkoutLength);
            	
            	ci.addNewBook(isbn, bookTitleString, maxLength, authors);
        		JOptionPane.showMessageDialog(
            			AddBookForm.this,
                        "Add book Successfull!!!",
                        "Info",
                        JOptionPane.INFORMATION_MESSAGE
                );
        		isbnField.setText("");
        		checkoutLengthField.setText("");
            	bookTitleField.setText("");
            }
        });
    }
    
    private void addRowToTable(Author author) {
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int rowIndex = authorTable.getSelectedRow();
                if (rowIndex != -1) {
                    authors.remove(rowIndex); // Remove author from list
                    tableModel.removeRow(rowIndex); // Remove row from table
                }
            }
        });
        
        // Add row to table with author info and remove button
        tableModel.addRow(new Object[]{
            authors.size(), // Author ID (for demonstration)
            author.getFirstName(),
            author.getLastName(),
            removeButton
        });
    }
}

class ButtonRenderer extends JButton implements TableCellRenderer {

    public ButtonRenderer() {
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value != null) ? value.toString() : "Remove");
        return this;
    }
}

// Button Editor class (for handling button clicks in the table)
class ButtonEditor extends DefaultCellEditor {
    protected JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;

    public ButtonEditor(JCheckBox checkBox, JTable table) {
        super(checkBox);
        this.table = table;
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        label = (value != null) ? value.toString() : "Remove";
        button.setText(label);
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            // When the button is clicked, remove the current row
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                ((DefaultTableModel) table.getModel()).removeRow(selectedRow);
            }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }

    @Override
    protected void fireEditingStopped() {
        super.fireEditingStopped();
    }
}

