package com.employee.ui;

import com.employee.service.EmployeeService;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class SearchPanel extends JPanel {
    private final EmployeeService employeeService;
    private final JTextField searchField;
    private final EmployeeListPanel employeeListPanel;

    public SearchPanel(EmployeeService employeeService) {
        this.employeeService = employeeService;
        this.employeeListPanel = new EmployeeListPanel(employeeService);
        
        setLayout(new MigLayout("fillx"));

        // Create search components
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        // Add components
        add(new JLabel("Search:"), "gap para");
        add(searchField, "growx");
        add(searchButton, "wrap");

        // Add action listeners
        searchButton.addActionListener(e -> performSearch());
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    performSearch();
                }
            }
        });
    }

    private void performSearch() {
        String query = searchField.getText().trim();
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                employeeListPanel.refreshData();
                return null;
            }
        };
        worker.execute();
    }
}
