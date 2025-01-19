package com.employee.ui;

import com.employee.model.Employee;
import com.employee.service.EmployeeService;
import net.miginfocom.swing.MigLayout;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class EmployeeListPanel extends JPanel {
    private final EmployeeService employeeService;
    private final JTable employeeTable;
    private final DefaultTableModel tableModel;
    private final JButton deleteButton;
    private boolean deletionEnabled = false;

    public EmployeeListPanel(EmployeeService employeeService) {
        this.employeeService = employeeService;
        setLayout(new MigLayout("fill"));

        // Create table model
        String[] columnNames = {"ID", "Name", "Department", "Job Title", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create table
        employeeTable = new JTable(tableModel);
        employeeTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Create delete button
        deleteButton = new JButton("Delete");
        deleteButton.setEnabled(false);
        deleteButton.addActionListener(e -> deleteSelectedEmployee());
        
        // Add scroll pane and button
        JScrollPane scrollPane = new JScrollPane(employeeTable);
        add(scrollPane, "grow, wrap");
        add(deleteButton, "align right");

        // Add selection listener
        employeeTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                deleteButton.setEnabled(deletionEnabled && employeeTable.getSelectedRow() != -1);
            }
        });

        // Initialize data
        refreshData();
    }

    public void refreshData() {
        SwingWorker<List<Employee>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<Employee> doInBackground() {
                return employeeService.searchEmployees("");
            }

            @Override
            protected void done() {
                try {
                    List<Employee> employees = get();
                    updateTableData(employees);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(EmployeeListPanel.this,
                            "Error loading employees: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        };
        worker.execute();
    }

    private void updateTableData(List<Employee> employees) {
        tableModel.setRowCount(0);
        for (Employee employee : employees) {
            Object[] rowData = {
                employee.getId(),
                employee.getFullName(),
                employee.getDepartment().getName(),
                employee.getJobTitle(),
                employee.getStatus()
            };
            tableModel.addRow(rowData);
        }
    }

    private void deleteSelectedEmployee() {
        int row = employeeTable.getSelectedRow();
        if (row != -1) {
            Long employeeId = (Long) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this employee?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    employeeService.deleteEmployee(employeeId);
                    refreshData();
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(this,
                            "Error deleting employee: " + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public void setDeletionEnabled(boolean enabled) {
        this.deletionEnabled = enabled;
        deleteButton.setEnabled(enabled && employeeTable.getSelectedRow() != -1);
    }
}
