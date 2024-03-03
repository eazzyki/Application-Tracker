import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;


public class MainFrame extends JFrame {
    private int WIDTH = 1920;
    private int HEIGHT = 1080;
    private JFrame newEntryFrame;
    private JFrame itemInfoFrame;
    private JFrame statsFrame;
    
    // Widgets
     private JPanel mainPanel;
     private JPanel topPanel;
     private JTable table;
     private JButton btnNewEntry;
     private JButton btnStats;
     private JLabel lblSort;
     private JComboBox<String> comboBoxSort;
     private JComboBox<String> comboBoxSortDir;
     
     public TableApplication model;


    public MainFrame() {
        init();
        newEntryFrame = new NewEntryFrame(MainFrame.this);
        model.sortTable(0, SortOrder.DESCENDING);
    }


    private void init() {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int widthScreen = gd.getDisplayMode().getWidth();
        int heightScreen = gd.getDisplayMode().getHeight();
        int width = (widthScreen>WIDTH)?WIDTH:widthScreen;
        int height = (heightScreen>HEIGHT)?HEIGHT:heightScreen;

        createFrame(width, height);

        // Button NewEntry click event
        btnNewEntry.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!newEntryFrame.isVisible()) {
                    newEntryFrame.setVisible(true);
                }
            }
        });

        // ComboBox changed event for sorting
        ActionListener comboBoxSortAction = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int columnToSortIdx = comboBoxSort.getSelectedIndex();
                int sortDir = comboBoxSortDir.getSelectedIndex();
                SortOrder order = ((sortDir==0) ? SortOrder.DESCENDING : SortOrder.ASCENDING);
                model.sortTable(columnToSortIdx, order);
                model.fireTableDataChanged();
            }
        }; 
        comboBoxSort.addActionListener(comboBoxSortAction);
        comboBoxSortDir.addActionListener(comboBoxSortAction);

        // Double Click on table event 
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    JTable target = (JTable) me.getSource();
                    int row = target.getSelectedRow();
                    if (itemInfoFrame != null) {
                        itemInfoFrame.dispose();
                    }
                    itemInfoFrame = new ItemInfoFrame(model, row);
                }
            }
        });

        // Button Stats click event
        btnStats.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (statsFrame != null) {
                    statsFrame.dispose();
                }
                statsFrame = new StatsFrame(MainFrame.this);
            }
        });

        // Closing event
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                model.writeToDatabase();
            }
        });

    }

    private void createFrame(int width, int height) {
        mainPanel = new JPanel();
        topPanel = new JPanel();
        model = new TableApplication();
        table = new MyTable(model);
        btnNewEntry = new JButton("New Application");

        // get icon for stats button 
        ImageIcon icon = new ImageIcon("stats_icon.png");
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(20, 20, java.awt.Image.SCALE_SMOOTH);
        icon = new ImageIcon(newImg);
        btnStats = new JButton(icon);
        
        lblSort = new JLabel("Sort by ");
        comboBoxSort = new JComboBox<String>(
            new String[] {"Date", "Company", "Position", "State"}
            );
        comboBoxSortDir = new JComboBox<String>(
            new String[] {"Descending", "Ascending"}
            );

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        topPanel.add(btnNewEntry);
        topPanel.add(Box.createRigidArea(new Dimension(25, 1)));
        topPanel.add(lblSort);
        comboBoxSort.setMaximumSize(new Dimension(100, 30));
        topPanel.add(comboBoxSort);
        comboBoxSortDir.setMaximumSize(new Dimension(100, 30));
        topPanel.add(comboBoxSortDir);
        topPanel.add(Box.createRigidArea(new Dimension(25, 1)));
        topPanel.add(btnStats);
    
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(topPanel);
        mainPanel.add(Box.createVerticalStrut(15));
        mainPanel.add(new JScrollPane(table));

        topPanel.setBorder(BorderFactory.createTitledBorder(""));
        mainPanel.setBorder(BorderFactory.createTitledBorder(""));

        add(mainPanel);
        setTitle("Job Application Tracker");
        setSize(width, height);
        setMinimumSize(new Dimension((int)(WIDTH*0.3), (int)(HEIGHT*0.3)));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Callbackfunction for NewEntryFrame to insert new item
    public void addItemToContainer(String[] input) {
        Item newItem = new Item(input.length+1);
        for (int i = 0; i < input.length; i++) {
            newItem.setValueAt(i, input[i]);
        }
        model.addNewItem(newItem);
    }
}
