package librarysystem;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ViewTitlesForm extends JPanel implements TitleCallback {

	private JList<String> titleList;
    private DefaultListModel<String> listModel;
    public ViewTitlesForm() {
        setLayout(new BorderLayout());

        // Create the title label
        JLabel titleLabel = new JLabel("View Titles", JLabel.LEFT);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.BLUE);
        add(titleLabel, BorderLayout.NORTH);

//        List<String> tmpList = this.callback.getTitles();
        // Sample data for the JList
//        JList<String> titleList = new JList<>(tmpList.toArray(new String[0]));
        listModel = new DefaultListModel<>();
        titleList = new JList<>(listModel);
        titleList.repaint();
        titleList.setFont(new Font("Arial", Font.PLAIN, 14));

        // Wrap the JList in a JScrollPane
        JScrollPane scrollPane = new JScrollPane(titleList);
        add(scrollPane, BorderLayout.CENTER);
        
     // Register this form as the data callback
        Data.setCallback(this);

        // Load initial data
        loadTitles();

        // Set preferred size (optional)
        setPreferredSize(new Dimension(400, 300));
    }
    
    // Method to load titles into the list
    private void loadTitles() {
        List<String> titles = Data.getBookTitles();
        listModel.clear();
        for (String title : titles) {
            listModel.addElement(title);
        }
    }

    // Callback method to refresh the list when data is updated
    @Override
    public void onDataUpdated() {
        loadTitles();
    }
}
