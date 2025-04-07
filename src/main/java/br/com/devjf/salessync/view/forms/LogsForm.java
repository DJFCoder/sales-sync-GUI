package br.com.devjf.salessync.view.forms;

import br.com.devjf.salessync.controller.LogController;
import br.com.devjf.salessync.model.SystemLog;
import br.com.devjf.salessync.model.UserActivity;
import br.com.devjf.salessync.service.UserActivityService;
import br.com.devjf.salessync.view.components.style.ViewComponentStyle;
import br.com.devjf.salessync.view.components.table.TableFormInterface;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.RowFilter;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

public class LogsForm extends javax.swing.JFrame implements TableFormInterface {
    private static final Logger LOGGER = Logger.getLogger(
            LogsForm.class.getName());
    private LogController logController;

    public LogsForm() {
        initComponents();
        this.logController = new LogController();
        setupTableColumns();
        setupTableFilters();
        loadTableData();
    }

    @Override
    public void setupTableColumns() {
        String[] columnNames = {
            "ID", "Data/Hora", "Usuário", "Ação", "Detalhes"
        };
        Class<?>[] columnClasses = {
            Integer.class, String.class, String.class, String.class, String.class
        };
        boolean[] editableColumns = {
            false, false, false, false, false
        };
        DefaultTableModel model = new DefaultTableModel(columnNames,
                0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnClasses[columnIndex];
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return editableColumns[column];
            }
        };
        logTable.setModel(model);
    }

    private void setupTableFilters() {
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(
                (DefaultTableModel) logTable.getModel());
        logTable.setRowSorter(sorter);
        // Listener for date/time filter
        dateTimeField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterLogs();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterLogs();
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterLogs();
            }

            private void filterLogs() {
                String text = dateTimeField.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text,
                            1));
                }
            }
        });
        // Listener for user filter
        userField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterLogs();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterLogs();
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterLogs();
            }

            private void filterLogs() {
                String text = userField.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text,
                            2));
                }
            }
        });
        // Listener for action filter
        actionField.getDocument().addDocumentListener(
                new javax.swing.event.DocumentListener() {
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterLogs();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterLogs();
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterLogs();
            }

            private void filterLogs() {
                String text = actionField.getText().trim();
                if (text.isEmpty()) {
                    sorter.setRowFilter(null);
                } else {
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text,
                            3));
                }
            }
        });
    }

    private void populateTableWithActivities(List<UserActivity> activities) {
        DefaultTableModel model = (DefaultTableModel) logTable.getModel();
        model.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(
                "dd/MM/yyyy HH:mm:ss");
        activities.forEach(activity -> {
            try {
                model.addRow(new Object[]{
                    activity.getId(),
                    formatDateTime(activity.getActivityTime(),
                    formatter),
                    getUserName(activity),
                    "ATIVIDADE",
                    formatDescription(activity.getDescription())
                });
            } catch (Exception e) {
                LOGGER.log(Level.WARNING,
                        "Error processing activity: " + activity,
                        e);
            }
        });
        LOGGER.log(Level.INFO,
                "Rows added to table: {0}",
                model.getRowCount());
    }

    private String formatDateTime(LocalDateTime dateTime, DateTimeFormatter formatter) {
        return dateTime != null ? dateTime.format(formatter) : "N/A";
    }

    private String getUserName(UserActivity activity) {
        return activity.getUser() != null ? activity.getUser().getName() : "Sistema";
    }

    private String formatDescription(String description) {
        return description != null ? description : "N/A";
    }

    @Override
    public void loadTableData() {
        SwingWorker<List<UserActivity>, Void> worker = new SwingWorker<>() {
            @Override
            protected List<UserActivity> doInBackground() {
                return logController.listUserActivities(null);
            }

            @Override
            protected void done() {
                try {
                    List<UserActivity> activities = get();
                    if (activities.isEmpty()) {
                        System.out.println(
                                "Nenhuma atividade encontrada no sistema.");
                        return;
                    }
                    populateTableWithActivities(activities);
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println(
                            "Erro ao carregar atividades: " + e.getMessage());
                }
            }
        };
        worker.execute();
    }

    @Override
    public void refreshTable() {
        loadTableData();
    }

    @Override
    public void setObjectToTable(Integer id) {
        // Not needed for logs form
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        filterLbl = new javax.swing.JLabel();
        filterPanel = new javax.swing.JPanel();
        dateTimeField = new javax.swing.JTextField();
        dateTimeLbl = new javax.swing.JLabel();
        actionLbl = new javax.swing.JLabel();
        actionField = new javax.swing.JTextField();
        userLbl = new javax.swing.JLabel();
        userField = new javax.swing.JTextField();
        tableScrollPanel = new javax.swing.JScrollPane();
        logTable = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        mainPanel.setBackground(new java.awt.Color(255, 255, 255));
        mainPanel.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 2, true));

        filterLbl.setFont(new java.awt.Font("Liberation Sans", 0, 18)); // NOI18N
        filterLbl.setText("Filtar");

        filterPanel.setPreferredSize(new java.awt.Dimension(917, 50));
        ViewComponentStyle.standardCornerRadius(filterPanel);

        dateTimeField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        dateTimeField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        dateTimeField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(dateTimeField);

        dateTimeLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        dateTimeLbl.setText("Data/Hora:");

        actionLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        actionLbl.setText("Ação:");

        actionField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        actionField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        actionField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(actionField);

        userLbl.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        userLbl.setText("Usuário:");

        userField.setFont(new java.awt.Font("Liberation Sans", 0, 14)); // NOI18N
        userField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        userField.setPreferredSize(new java.awt.Dimension(121, 30));
        ViewComponentStyle.standardCornerRadius(userField);

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
                filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(filterPanelLayout.createSequentialGroup()
                                .addGap(120, 120, 120)
                                .addComponent(userLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE, 181,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(dateTimeLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dateTimeField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(actionLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(actionField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(121, Short.MAX_VALUE)));
        filterPanelLayout.setVerticalGroup(
                filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(filterPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(filterPanelLayout
                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(filterPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(dateTimeLbl)
                                                .addComponent(dateTimeField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(filterPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(actionLbl)
                                                .addComponent(actionField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(filterPanelLayout
                                                .createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                .addComponent(userLbl)
                                                .addComponent(userField, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(10, 10, 10)));

        logTable.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {

                },
                new String[] {
                        "ID", "Data/Hora", "Usuário", "Ação", "Detalhes"
                }));
        tableScrollPanel.setViewportView(logTable);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 917,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(filterPanel, javax.swing.GroupLayout.Alignment.CENTER,
                                                javax.swing.GroupLayout.PREFERRED_SIZE, 916,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(filterLbl, javax.swing.GroupLayout.Alignment.CENTER))
                                .addContainerGap(19, Short.MAX_VALUE)));
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(mainPanelLayout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(filterLbl)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(tableScrollPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 520,
                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(48, Short.MAX_VALUE)));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap()));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField actionField;
    private javax.swing.JLabel actionLbl;
    private javax.swing.JTextField dateTimeField;
    private javax.swing.JLabel dateTimeLbl;
    private javax.swing.JLabel filterLbl;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTable logTable;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane tableScrollPanel;
    private javax.swing.JTextField userField;
    private javax.swing.JLabel userLbl;
    // End of variables declaration//GEN-END:variables
}
