import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewEntryFrame extends JFrame {
    private JPanel mainPanel;
    private JPanel panelButtons;
    private JLabel lblDate;
    private JLabel lblCompany;
    private JLabel lblPosition;
    private JTextField txtFieldDate;
    private JTextField txtFieldCompany;
    private JTextField txtFieldPosition;
    private JButton btnAdd;
    private JButton btnCancel;
    private SimpleDateFormat formatter;

    private MainFrame parent;
    private int WIDTH_OF_TXTFIELD = 35;
    
    public NewEntryFrame(MainFrame mainFrame) {
        this.parent = mainFrame;
        init();
    }

    private void init() {
        createFrame();

        // Button Cancel
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                reset();
            }
        });

        // Button Add
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (areInputsValid()) {
                    String[] inputs = {txtFieldDate.getText(),
                        txtFieldCompany.getText(),
                        txtFieldPosition.getText()};
                        parent.addItemToContainer(inputs);
                }
                dispose();
                reset();
                parent.model.fireTableDataChanged();
            }
        });

        // Reset TxtFields after closing event
        addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        reset();
                    }
                });
    }

    private void createFrame() {
        mainPanel = new JPanel();
        panelButtons = new JPanel();
        lblDate = new JLabel("Date:        ");
        lblCompany = new JLabel("Company:");
        lblPosition = new JLabel("Position:  ");
        txtFieldDate = new JTextField();
        txtFieldCompany = new JTextField();
        txtFieldPosition = new JTextField();
        btnAdd = new JButton("Add");
        btnCancel = new JButton("Cancel");
        formatter = new SimpleDateFormat("dd.MM.yyyy");
        formatter.setLenient(false);

        // Text fields
        txtFieldDate.setText(formatter.format(new Date()));
        txtFieldDate.setColumns(WIDTH_OF_TXTFIELD);
        txtFieldCompany.setColumns(WIDTH_OF_TXTFIELD);
        txtFieldPosition.setColumns(WIDTH_OF_TXTFIELD);

        // Buttons 
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        panelButtons.add(btnCancel);
        panelButtons.add(Box.createHorizontalStrut(20));
        panelButtons.add(btnAdd);

        // Layout
        GridBagLayout gbl = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        gbl.setConstraints(mainPanel, gbc);
        mainPanel.setLayout(gbl);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(lblDate, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(txtFieldDate, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(lblCompany, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(txtFieldCompany, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(lblPosition, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        mainPanel.add(txtFieldPosition, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(Box.createVerticalStrut(20), gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panelButtons, gbc);

        add(mainPanel);
        setTitle("New Application");
        setResizable(false);
        setMinimumSize(new Dimension(500, 200));
        setLocationRelativeTo(parent);
    }

    private void reset() {
        txtFieldDate.setText(formatter.format(new Date()));
        txtFieldCompany.setText("");
        txtFieldPosition.setText("");
    }

    private boolean areInputsValid() {
        try {
            String inputDate = txtFieldDate.getText();
            formatter.parse(inputDate.trim());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "The date is not valid!", "Wrong Input", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
}
