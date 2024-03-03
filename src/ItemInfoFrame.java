import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ItemInfoFrame extends JFrame {
    private MainFrame parent;
    private TableApplication table;
    private Item item;
    private int row;

    private JPanel mainPanel;
    private JPanel panelButtons;
    private JLabel lblDate;
    private JLabel lblCompany;
    private JLabel lblPosition;
    private JLabel lblState;
    private JComboBox<String> comboBoxState;
    private JTextField txtFieldDate;
    private JTextField txtFieldCompany;
    private JTextField txtFieldPosition;
    private JButton btnUpdate;
    private JButton btnDelete;
    private SimpleDateFormat formatter;
    
    private int WIDTH_OF_TXTFIELD = 35;

    public ItemInfoFrame(TableApplication table, int row) {
        this.table = table;
        this.row = row;
        init();
    }

    private void init() {
        int numColumns = table.getColumnCount();
        item = new Item(numColumns);
        for (int i = 0; i < numColumns; i++) {
            Object value = table.getValueAt(row, i);
            item.setValueAt(i, value);
        }

        createFrame();

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (areInputsValid()) {
                    String[] inputs = {txtFieldDate.getText(),
                        txtFieldCompany.getText(),
                        txtFieldPosition.getText(), 
                        comboBoxState.getSelectedItem().toString()};
                        table.updateItem(inputs, ItemInfoFrame.this.row);
                }
                dispose();
                table.fireTableDataChanged();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int optionType = JOptionPane.OK_CANCEL_OPTION;
                int result = JOptionPane.showConfirmDialog(ItemInfoFrame.this, "Do you really want to delete the Item?", "Confirm", optionType);
                if (result == JOptionPane.OK_OPTION) {
                    table.deleteItem(ItemInfoFrame.this.row);
                    table.fireTableDataChanged();
                    dispose();
                } 
            }
        });

    }

    private void createFrame() {
        mainPanel = new JPanel();
        panelButtons = new JPanel();
        lblDate = new JLabel("Date: ");
        lblCompany = new JLabel("Company: ");
        lblPosition = new JLabel("Position: ");
        lblState = new JLabel("State: ");
        comboBoxState = new JComboBox<String>(
            new String[] {"ONGOING", "REJECTED", "INVITED"}
            );
        txtFieldDate = new JTextField();
        txtFieldCompany = new JTextField();
        txtFieldPosition = new JTextField();
        btnUpdate = new JButton("Update");
        btnDelete = new JButton("Delete");
        formatter = new SimpleDateFormat("dd.MM.YYYY");
        formatter.setLenient(false);

        // Date
        txtFieldDate.setColumns(WIDTH_OF_TXTFIELD);
        txtFieldDate.setText(item.getValueAt(0).toString());

        // Company
        txtFieldCompany.setColumns(WIDTH_OF_TXTFIELD);
        txtFieldCompany.setText(item.getValueAt(1).toString());

        // Position
        txtFieldPosition.setColumns(WIDTH_OF_TXTFIELD);
        txtFieldPosition.setText(item.getValueAt(2).toString());

        // State
        comboBoxState.setSelectedItem(item.getState().toString());

        // Buttons
        panelButtons.setLayout(new BoxLayout(panelButtons, BoxLayout.X_AXIS));
        panelButtons.add(btnDelete);
        panelButtons.add(Box.createHorizontalStrut(20));
        panelButtons.add(btnUpdate);

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
        mainPanel.add(lblState, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        mainPanel.add(comboBoxState, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        mainPanel.add(Box.createVerticalStrut(20), gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(panelButtons, gbc);

        add(mainPanel);
        setMinimumSize(new Dimension(500, 200));
        setLocationRelativeTo(parent);
        setVisible(true);
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
