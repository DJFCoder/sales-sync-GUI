package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.controller.UserController;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.model.UserType;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.components.table.TableFormInterface;
import br.com.devjf.salessync.view.components.table.TableManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class UsersForm extends javax.swing.JFrame implements TableFormInterface {
    private static UsersForm instance;
    private UserController userController;

    public UsersForm() {
        this.userController = new UserController();
        instance = this;
        initComponents();
        setupTableColumns();
        TableManager.setupFilters(nameField,
                statusField,
                userTypeField,
                userTable);
        loadTableData();
    }

    public static UsersForm getInstance() {
        return instance;
    }

    @Override
    public final void setupTableColumns() {
        // Define column names
        String[] columnNames = {
            "Código", "Nome", "Login", "Tipo", "Status", "Ações"
        };
        // Define column classes
        Class<?>[] columnClasses = {
            Integer.class, String.class, String.class, String.class, String.class, String.class
        };
        // Define which columns are editable
        boolean[] editableColumns = {
            false, false, false, false, false, true
        };
        // Setup table model
        TableManager.setupTableModel(userTable,
                columnNames,
                columnClasses,
                editableColumns);
        // Setup edit button
        setupEditButton();
        // Configure table selection behavior
        configureTableSelectionBehavior();
    }

    @Override
    public final void loadTableData() {
        try {
            // Clear table
            TableManager.clearTable(
                    (DefaultTableModel) userTable.getModel());
            // Get filter values
            Map<String, Object> filters = new HashMap<>();
            String nameFilter = nameField.getText().trim();
            String statusFilter = statusField.getText().trim();
            String typeFilter = userTypeField.getText().trim();
            // Add filters if they are not empty
            if (!nameFilter.isEmpty()) {
                filters.put("name",
                        nameFilter);
            }
            // Modify status filter handling
            if (!statusFilter.isEmpty()) {
                // Check if filter is for active or inactive users
                if (statusFilter.equalsIgnoreCase("ativo")) {
                    filters.put("active",
                            true);
                } else if (statusFilter.equalsIgnoreCase("inativo")) {
                    filters.put("active",
                            false);
                }
            }
            if (!typeFilter.isEmpty()) {
                filters.put("type",
                        getUserTypeFromDisplayName(typeFilter));
            }
            // Get users through controller
            List<User> users;
            if (filters.isEmpty()) {
                // If no filters, fetch all users including inactive
                users = userController.listAllUsers(new HashMap<>());
            } else {
                users = userController.listAllUsers(filters);
            }
            // Add users to table
            if (users != null) {
                for (User user : users) {
                    setObjectToTable(user.getId());
                }
            }
            // Adjust column widths
            TableManager.adjustColumnWidths(userTable,
                    10);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao carregar dados da tabela: " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erro ao carregar usuários: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Convert display name to UserType
     *
     * @param displayName Display name of user type
     * @return Corresponding UserType
     */
    private UserType getUserTypeFromDisplayName(String displayName) {
        switch (displayName) {
            case "Administrador":
                return UserType.ADMIN;
            case "Proprietário":
                return UserType.OWNER;
            case "Colaborador":
                return UserType.EMPLOYEE;
            default:
                return null;
        }
    }

    @Override
    public void refreshTable() {
        loadTableData();
    }

    /**
     * Convert user type to display name
     *
     * @param type UserType enum
     * @return Display name for user type
     */
    private String getUserTypeDisplayName(UserType type) {
        switch (type) {
            case ADMIN:
                return "Administrador";
            case OWNER:
                return "Proprietário";
            case EMPLOYEE:
                return "Colaborador";
            default:
                return "";
        }
    }

    @Override
    public void setObjectToTable(Integer userId) {
        try {
            // Get user data
            User user = userController.findUserById(userId);
            if (user == null) {
                return;
            }
            Object[] rowData = {
                user.getId(),
                user.getName(),
                user.getLogin(),
                getUserTypeDisplayName(user.getType()),
                user.isActive() ? "Ativo" : "Inativo",
                "Editar"
            };
            TableManager.addRow(
                    (DefaultTableModel) userTable.getModel(),
                    rowData);
        } catch (Exception e) {
            System.err.println(
                    "Erro ao adicionar usuário à tabela: " + e.getMessage());
            e.printStackTrace(); // Add stack trace for better debugging
        }
    }

    /**
     * Configure table selection behavior to improve user experience
     */
    private void configureTableSelectionBehavior() {
        // Allow selection of only one row at a time
        userTable.setSelectionMode(
                javax.swing.ListSelectionModel.SINGLE_SELECTION);
        // Configure to start editing immediately on cell click
        userTable.putClientProperty("terminateEditOnFocusLost",
                Boolean.TRUE);
        // Allow editing of the button cell with a single click
        userTable.setEditingColumn(5);
    }

    /**
     * Setup edit button in the actions column
     */
    private void setupEditButton() {
        // Get the actions column (last column)
        TableColumn actionColumn = userTable.getColumnModel().getColumn(5);
        // Configure renderer and editor for the actions column
        actionColumn.setCellRenderer(
                new br.com.devjf.salessync.view.components.table.TableEditButtonRenderer(
                        "Editar"));
        actionColumn.setCellEditor(
                new br.com.devjf.salessync.view.components.table.TableEditButtonEditor(
                        "Editar",
                        row -> editUser(row)));
        // Set preferred width for the actions column
        actionColumn.setPreferredWidth(80);
        actionColumn.setMaxWidth(100);
        actionColumn.setMinWidth(80);
    }

    /**
     * Open edit form for the selected user
     *
     * @param selectedRow The index of the selected row in the table
     */
    private void editUser(int selectedRow) {
        try {
            // Get the ID of the selected user
            Integer userId = (Integer) userTable.getValueAt(selectedRow,
                    0);
            System.out.println("Editando usuário com ID: " + userId);
            // Open the NewUserForm in edit mode with the selected user
            User user = userController.findUserById(userId);
            MainAppView.redirectToPanel(MainAppView.EDIT_USER_PANEL,
                    user);
        } catch (Exception e) {
            System.err.println("Erro ao editar usuário: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Erro ao editar usuário: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        filterLbl = new javax.swing.JLabel();
        newUserButton = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        statusField = new javax.swing.JTextField();
        statusLbl = new javax.swing.JLabel();
        userTypeLbl = new javax.swing.JLabel();
        userTypeField = new javax.swing.JTextField();
        nameLbl = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        tableScrollPanel = new javax.swing.JScrollPane();
        userTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        filterLbl.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        filterLbl.setText("Filtar");

        newUserButton.setBackground(new java.awt.Color(76, 175, 80));
        newUserButton.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        newUserButton.setForeground(new java.awt.Color(255, 255, 255));
        newUserButton.setText("Novo Usuário");
        newUserButton.setPreferredSize(new java.awt.Dimension(150, 40));
        ViewComponentStyle.standardCornerRadius(newUserButton);
        newUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newUserButtonActionPerformed(evt);
            }
        });

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewComponentStyle.standardCornerRadius(filterPanel);

        statusField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        statusField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        statusField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(statusField);

        statusLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        statusLbl.setText("Status:");

        userTypeLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        userTypeLbl.setText("Tipo:");

        userTypeField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        userTypeField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userTypeField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(userTypeField);

        nameLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        nameLbl.setText("Nome:");

        nameField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        nameField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        nameField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(nameField);

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(nameLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(statusLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(statusField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(userTypeLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(userTypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(140, Short.MAX_VALUE))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(statusLbl)
                        .addComponent(statusField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(userTypeLbl)
                        .addComponent(userTypeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLbl)
                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        userTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Login", "Tipo", "Status", "Ações"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tableScrollPanel.setViewportView(userTable);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(newUserButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(tableScrollPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 917, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(filterPanel, javax.swing.GroupLayout.Alignment.CENTER, javax.swing.GroupLayout.PREFERRED_SIZE, 916, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterLbl, javax.swing.GroupLayout.Alignment.CENTER))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(filterLbl)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 441, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(newUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(69, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void newUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newUserButtonActionPerformed
        MainAppView.redirectToPanel(MainAppView.NEW_USER_PANEL);
    }//GEN-LAST:event_newUserButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLbl;
    private javax.swing.JButton newUserButton;
    private javax.swing.JTextField statusField;
    private javax.swing.JLabel statusLbl;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JTable userTable;
    private javax.swing.JTextField userTypeField;
    private javax.swing.JLabel userTypeLbl;
    // End of variables declaration//GEN-END:variables
}
