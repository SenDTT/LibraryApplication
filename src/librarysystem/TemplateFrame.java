package librarysystem;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.*;

import dataaccess.User;

public class TemplateFrame extends JFrame implements DataCallback {
	JList<String> linkList;
	//context for CardLayout
	JPanel cards;
	JPanel buttonBar;
	private boolean[] enabled;
	private BufferedImage backgroundImage;
	private BufferedImage backgroundMenu;
	private User user;

	public JList<String> getLinkList() {
		return this.linkList;
	}
	
	public void setEnable(boolean[] enabled) {
		this.enabled = enabled;
	}
	
	@Override
    public void onLoginSuccess(boolean[] menuEnabled) {
        for (int i = 0; i < menuEnabled.length; i++) {
            enabled[i] = menuEnabled[i];
        }
        linkList.repaint();
    }
	
	@Override
    public void setUser(User user) {
        this.user = user;
    }
	
	@Override
    public User getUser() {
        return this.user;
    }
	
	
	public void setJListMenu(boolean[] enabled) {
		linkList.setSelectionModel(new DefaultListSelectionModel() {
            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (index0 == index1 && enabled[index0]) {
                    super.setSelectionInterval(index0, index1);
                }
            }
        });
	}
	
	public TemplateFrame() {
		try {
            String currDirectory = System.getProperty("user.dir");
            backgroundImage = ImageIO.read(new File(currDirectory + "/src/librarysystem/leftpanel.png"));
            backgroundMenu = ImageIO.read(new File(currDirectory + "/src/images/bg_menu.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

		setSize(800, 600);
		String[] items = {"Login", "Add Library Member", "Checkout", "Add Book", "Add Book Copy", "Show Overdue Book", "Print Checkout Record", "Add Author"};
		linkList = new JList<String>(items);	
		boolean[] menuEnable = {true, false, false, false, false, false, false, false};
		this.setEnable(menuEnable);
		setJListMenu(menuEnable);
		createPanels();	
		linkList.setSelectedIndex(0);
		
		// set up split panes
		JSplitPane splitPane = new JSplitPane(
			JSplitPane.HORIZONTAL_SPLIT, linkList, cards);
		 // Create the left panel with a background image
        JPanel linkList = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
		splitPane.setDividerLocation(150);
		//default layout for JFrame is BorderLayout; add method 
		//adds to contentpane
		add(splitPane, BorderLayout.CENTER);

		JTextArea textArea = new JTextArea(3,600);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("Welcome to Library System!");
        textArea.setForeground(Color.BLUE);
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.SOUTH);
	}
	
	private void showLoginForm() {
        LoginForm loginForm = new LoginForm(this);
        // Show the login form in a dialog
        JOptionPane.showConfirmDialog(this, loginForm, "Login", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    }

	
	/* Organize panels into a CardLayout */
	public void createPanels() {

		LoginForm loginForm = new LoginForm(this);
		ViewTitlesForm viewTitlesForm = new ViewTitlesForm();
		AddBookForm addBookForm = new AddBookForm();
		AddBookCopyForm addBookCopyForm = new AddBookCopyForm();
		CheckoutForm checkoutForm = new CheckoutForm(this);
		AddMemberForm addMemberForm = new AddMemberForm();
		AddAuthorForm addAuthorForm = new AddAuthorForm();
		DueDateForm dueDateForm = new DueDateForm(this);
		PrintCheckoutRecordForm printCheckoutRecordForm = new PrintCheckoutRecordForm(this);

		cards = new JPanel(new CardLayout());
		cards.add(loginForm, "Login");
		cards.add(viewTitlesForm, "View Titles");
		cards.add(addAuthorForm, "Add Author");
		cards.add(addBookForm, "Add Book");
		cards.add(addBookCopyForm, "Add Book Copy");
		cards.add(checkoutForm, "Checkout");
		cards.add(addMemberForm, "Add Library Member");
		cards.add(dueDateForm, "Show Overdue Book");
		cards.add(printCheckoutRecordForm, "Print Checkout Record");
		
		// Connect JList elements to CardLayout panels
		linkList.addListSelectionListener(event -> {
			String value = linkList.getSelectedValue().toString();
			CardLayout cl = (CardLayout) (cards.getLayout());
			cl.show(cards, value);
		});
	}

	private static final long serialVersionUID = -760156396736751840L;
	
}
