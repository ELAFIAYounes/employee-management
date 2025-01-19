package com.employee.ui;

import com.employee.model.Employee;
import com.employee.model.EmploymentStatus;
import com.employee.service.EmployeeService;
import net.miginfocom.swing.MigLayout;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Component
public class EmployeeDetailPanel extends JPanel {
    private final EmployeeService employeeService;
    private Employee currentEmployee;
    private boolean editable = true;

    private final JTextField idField;
    private final JTextField nameField;
    private final JTextField jobTitleField;
    private final JComboBox<String> departmentCombo;
    private final JTextField hireDateField;
    private final JComboBox<EmploymentStatus> statusCombo;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JTextArea addressArea;
    private final JButton saveButton;
    private final JButton cancelButton;

    public EmployeeDetailPanel(EmployeeService employeeService) {
        this.employeeService = employeeService;
        setLayout(new MigLayout("fillx"));

        // Initialize components
        idField = new JTextField(20);
        nameField = new JTextField(20);
        jobTitleField = new JTextField(20);
        departmentCombo = new JComboBox<>(new String[]{"Engineering", "HR", "Finance"});
        hireDateField = new JTextField(10);
        statusCombo = new JComboBox<>(EmploymentStatus.values());
        emailField = new JTextField(20);
        phoneField = new JTextField(20);
        addressArea = new JTextArea(4, 20);
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        // Set up components
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);
        JScrollPane addressScroll = new JScrollPane(addressArea);

        // Layout components
        add(createLabeledComponent("Employee ID:", idField), "gap para, wrap");
        add(createLabeledComponent("Full Name:", nameField), "gap para, wrap");
        add(createLabeledComponent("Job Title:", jobTitleField), "gap para, wrap");
        add(createLabeledComponent("Department:", departmentCombo), "gap para, wrap");
        add(createLabeledComponent("Hire Date:", hireDateField), "gap para, wrap");
        add(createLabeledComponent("Status:", statusCombo), "gap para, wrap");
        add(createLabeledComponent("Email:", emailField), "gap para, wrap");
        add(createLabeledComponent("Phone:", phoneField), "gap para, wrap");
        add(createLabeledComponent("Address:", addressScroll), "gap para, wrap");

        // Add buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, "span, center");

        // Add action listeners
        saveButton.addActionListener(e -> saveEmployee());
        cancelButton.addActionListener(e -> clearForm());

        // Initial state
        updateEditableState();
    }

    private JPanel createLabeledComponent(String labelText, JComponent component) {
        JPanel panel = new JPanel(new MigLayout("insets 0"));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, label.getPreferredSize().height));
        panel.add(label);
        panel.add(component, "growx");
        return panel;
    }

    public void setEmployee(Employee employee) {
        this.currentEmployee = employee;
        if (employee != null) {
            SwingUtilities.invokeLater(() -> {
                idField.setText(employee.getEmployeeId());
                nameField.setText(employee.getFullName());
                jobTitleField.setText(employee.getJobTitle());
                departmentCombo.setSelectedItem(employee.getDepartment().getName());
                hireDateField.setText(employee.getHireDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
                statusCombo.setSelectedItem(employee.getStatus());
                emailField.setText(employee.getEmail());
                phoneField.setText(employee.getPhone());
                addressArea.setText(employee.getAddress());
                updateEditableState();
            });
        } else {
            clearForm();
        }
    }

    public void setEditable(boolean editable) {
        if (this.editable != editable) {
            this.editable = editable;
            updateEditableState();
        }
    }

    private void updateEditableState() {
        SwingUtilities.invokeLater(() -> {
            boolean isNew = currentEmployee == null;
            
            // ID field is only editable for new employees
            idField.setEditable(editable && isNew);
            
            // Other fields follow the general editable state
            nameField.setEditable(editable);
            jobTitleField.setEditable(editable);
            departmentCombo.setEnabled(editable);
            hireDateField.setEditable(editable);
            statusCombo.setEnabled(editable);
            emailField.setEditable(editable);
            phoneField.setEditable(editable);
            addressArea.setEditable(editable);
            
            // Buttons are only enabled when editable
            saveButton.setEnabled(editable);
            cancelButton.setEnabled(editable);

            // Update visual appearance
            Arrays.asList(idField, nameField, jobTitleField, hireDateField, 
                         emailField, phoneField, addressArea)
                .forEach(component -> {
                    component.setBackground(editable ? Color.WHITE : Color.LIGHT_GRAY);
                    component.setForeground(editable ? Color.BLACK : Color.DARK_GRAY);
                });
        });
    }

    private void saveEmployee() {
        if (!validateInput()) {
            return;
        }

        try {
            if (currentEmployee == null) {
                currentEmployee = new Employee();
            }

            currentEmployee.setEmployeeId(idField.getText());
            currentEmployee.setFullName(nameField.getText());
            currentEmployee.setJobTitle(jobTitleField.getText());
            currentEmployee.setHireDate(LocalDate.parse(hireDateField.getText()));
            currentEmployee.setStatus((EmploymentStatus) statusCombo.getSelectedItem());
            currentEmployee.setEmail(emailField.getText());
            currentEmployee.setPhone(phoneField.getText());
            currentEmployee.setAddress(addressArea.getText());

            Employee savedEmployee = currentEmployee.getId() == null ?
                employeeService.createEmployee(currentEmployee) :
                employeeService.updateEmployee(currentEmployee.getId(), currentEmployee);

            JOptionPane.showMessageDialog(this, 
                "Employee saved successfully!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
            
            setEmployee(savedEmployee);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Error saving employee: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            showError("Full name is required");
            return false;
        }
        if (jobTitleField.getText().trim().isEmpty()) {
            showError("Job title is required");
            return false;
        }
        if (!isValidEmail(emailField.getText())) {
            showError("Invalid email format");
            return false;
        }
        try {
            LocalDate.parse(hireDateField.getText());
        } catch (Exception e) {
            showError("Invalid hire date format (use YYYY-MM-DD)");
            return false;
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    private void showError(String message) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, 
                message, 
                "Validation Error", 
                JOptionPane.ERROR_MESSAGE);
        });
    }

    private void clearForm() {
        SwingUtilities.invokeLater(() -> {
            currentEmployee = null;
            idField.setText("");
            nameField.setText("");
            jobTitleField.setText("");
            departmentCombo.setSelectedIndex(-1);
            hireDateField.setText("");
            statusCombo.setSelectedIndex(0);
            emailField.setText("");
            phoneField.setText("");
            addressArea.setText("");
            updateEditableState();
        });
    }
}
