package com.employee.ui;

import com.employee.service.EmployeeService;
import net.miginfocom.swing.MigLayout;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Component
public class MainFrame extends JFrame implements LoginListener {
    private final EmployeeService employeeService;
    private final LoginPanel loginPanel;
    
    // Lazy initialization of panels
    private EmployeeListPanel employeeListPanel;
    private EmployeeDetailPanel employeeDetailPanel;
    private SearchPanel searchPanel;
    
    private final CardLayout cardLayout;
    private final JPanel mainPanel;
    private final JPanel contentPanel;

    public MainFrame(EmployeeService employeeService, LoginPanel loginPanel) {
        this.employeeService = employeeService;
        this.loginPanel = loginPanel;
        this.cardLayout = new CardLayout();
        this.contentPanel = new JPanel(cardLayout);
        this.mainPanel = new JPanel(new MigLayout("fill"));

        // Set up login listener
        loginPanel.setLoginListener(this);

        initializeUI();
    }

    @Override
    public void onLoginSuccess() {
        showMainPanel();
        updateUIBasedOnRole();
    }

    private void initializeUI() {
        setTitle("Employee Records Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create main content panel with card layout
        contentPanel.add(loginPanel, "login");
        contentPanel.add(mainPanel, "main");
        add(contentPanel, BorderLayout.CENTER);

        // Add window listener for logout on close
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                logout();
            }
        });

        // Set window size and position
        setSize(1024, 768);
        setLocationRelativeTo(null);

        // Show login panel initially
        showLoginPanel();
    }

    private void initializeMainPanelComponents() {
        if (employeeListPanel == null) {
            employeeListPanel = new EmployeeListPanel(employeeService);
        }
        if (employeeDetailPanel == null) {
            employeeDetailPanel = new EmployeeDetailPanel(employeeService);
        }
        if (searchPanel == null) {
            searchPanel = new SearchPanel(employeeService);
        }
    }

    private void initializeMainPanel() {
        initializeMainPanelComponents();
        
        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // Create main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                createLeftPanel(), createRightPanel());
        splitPane.setDividerLocation(300);

        mainPanel.removeAll();
        mainPanel.add(splitPane, "grow");
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel(new MigLayout("fill"));
        leftPanel.add(searchPanel, "wrap, growx");
        leftPanel.add(employeeListPanel, "grow");
        return leftPanel;
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel(new MigLayout("fill"));
        rightPanel.add(employeeDetailPanel, "grow");
        return rightPanel;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exportItem = new JMenuItem("Export Data");
        JMenuItem logoutItem = new JMenuItem("Logout");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);

        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem addItem = new JMenuItem("Add Employee");
        JMenuItem deleteItem = new JMenuItem("Delete Employee");
        editMenu.add(addItem);
        editMenu.add(deleteItem);

        // Reports menu
        JMenu reportsMenu = new JMenu("Reports");
        JMenuItem departmentReport = new JMenuItem("Department Report");
        JMenuItem statusReport = new JMenuItem("Status Report");
        reportsMenu.add(departmentReport);
        reportsMenu.add(statusReport);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(reportsMenu);

        // Add action listeners
        logoutItem.addActionListener(e -> logout());
        exitItem.addActionListener(e -> System.exit(0));

        return menuBar;
    }

    public void showLoginPanel() {
        cardLayout.show(contentPanel, "login");
        setJMenuBar(null);
        loginPanel.reset();
    }

    private void showMainPanel() {
        initializeMainPanel();
        cardLayout.show(contentPanel, "main");
    }

    public void updateUIBasedOnRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        boolean isHR = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_HR"));
        boolean isManager = auth.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_MANAGER"));

        // Update menu items visibility
        JMenuBar menuBar = getJMenuBar();
        if (menuBar != null) {
            // File menu
            JMenu fileMenu = menuBar.getMenu(0);
            fileMenu.getItem(0).setEnabled(isAdmin || isHR); // Export

            // Edit menu
            JMenu editMenu = menuBar.getMenu(1);
            editMenu.setEnabled(isAdmin || isHR);

            // Reports menu
            JMenu reportsMenu = menuBar.getMenu(2);
            reportsMenu.setEnabled(isAdmin || isHR || isManager);
        }

        // Update panels
        if (employeeDetailPanel != null) {
            employeeDetailPanel.setEditable(isAdmin || isHR);
        }
        if (employeeListPanel != null) {
            employeeListPanel.setDeletionEnabled(isAdmin);
        }
    }

    private void logout() {
        SecurityContextHolder.clearContext();
        showLoginPanel();
    }
}
