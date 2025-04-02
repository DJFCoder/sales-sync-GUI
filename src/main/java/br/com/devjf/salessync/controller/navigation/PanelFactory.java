package br.com.devjf.salessync.controller.navigation;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import br.com.devjf.salessync.model.Customer;
import br.com.devjf.salessync.model.Expense;
import br.com.devjf.salessync.model.Sale;
import br.com.devjf.salessync.model.ServiceOrder;
import br.com.devjf.salessync.model.User;
import br.com.devjf.salessync.view.MainAppView;
import br.com.devjf.salessync.view.forms.CustomersForm;
import br.com.devjf.salessync.view.forms.DashboardForm;
import br.com.devjf.salessync.view.forms.ExpensesForm;
import br.com.devjf.salessync.view.forms.LogsForm;
import br.com.devjf.salessync.view.forms.ReportsForm;
import br.com.devjf.salessync.view.forms.SalesForm;
import br.com.devjf.salessync.view.forms.ServiceOrdersForm;
import br.com.devjf.salessync.view.forms.UsersForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewCustomerForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewExpenseForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewSaleForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewServiceOrderForm;
import br.com.devjf.salessync.view.forms.newobjectforms.NewUserForm;

/**
 * Factory class for creating panels based on panel keys.
 */
public class PanelFactory {
    /**
     * Creates a panel based on the panel key with optional object parameters.
     *
     * @param panelKey The key identifying which panel to create
     * @param objects Optional objects needed for panel creation
     * @return The created panel or null if the key is not recognized
     */
    public JPanel createPanel(String panelKey, Object... objects) {
        try {
            // Extract objects from the varargs if provided
            Sale sale = null;
            Customer customer = null;
            ServiceOrder serviceOrder = null;
            Expense expense = null;
            User user = null;
            // Check each object and assign to the appropriate variable
            for (Object obj : objects) {
                if (obj instanceof Sale) {
                    sale = (Sale) obj;
                } else if (obj instanceof Customer) {
                    customer = (Customer) obj;
                } else if (obj instanceof ServiceOrder) {
                    serviceOrder = (ServiceOrder) obj;
                } else if (obj instanceof Expense) {
                    expense = (Expense) obj;
                } else if (obj instanceof User) {
                    user = (User) obj;
                }
            }
            JFrame form = null;
            switch (panelKey) {
                case MainAppView.DASHBOARD_PANEL:
                    form = new DashboardForm();
                    break;
                case MainAppView.SALES_PANEL:
                    form = new SalesForm();
                    break;
                case MainAppView.CUSTOMERS_PANEL:
                    form = new CustomersForm();
                    break;
                case MainAppView.SERVICE_ORDERS_PANEL:
                    form = new ServiceOrdersForm();
                    break;
                case MainAppView.EXPENSES_PANEL:
                    form = new ExpensesForm();
                    break;
                case MainAppView.REPORTS_PANEL:
                    form = new ReportsForm();
                    break;
                case MainAppView.USERS_PANEL:
                    form = new UsersForm();
                    break;
                case MainAppView.SYSTEM_LOGS_PANEL:
                    form = new LogsForm();
                    break;
                // New object forms
                case MainAppView.NEW_SALE_PANEL:
                    form = new NewSaleForm();
                    break;
                case MainAppView.EDIT_SALE_PANEL:
                    if (sale != null) {
                        form = new NewSaleForm(sale);
                    } else {
                        System.err.println(
                                "Erro: Tentativa de editar venda sem fornecer objeto Sale");
                        return null;
                    }
                    break;
                case MainAppView.NEW_CUSTOMER_PANEL:
                    form = new NewCustomerForm();
                    break;
                case MainAppView.EDIT_CUSTOMER_PANEL:
                    if (customer == null) {
                        System.err.println(
                                "Erro: Tentativa de editar cliente sem fornecer objeto Customer");
                        return null;
                    }
                    form = new NewCustomerForm(customer.getId());
                    break;
                case MainAppView.NEW_SO_PANEL:
                    form = new NewServiceOrderForm();
                    break;
                case MainAppView.EDIT_SO_PANEL:
                    if (serviceOrder != null) {
                        form = new NewServiceOrderForm(serviceOrder);
                    } else {
                        System.err.println(
                                "Erro: Tentativa de editar ordem de serviço sem fornecer objeto ServiceOrder");
                        return null;
                    }
                    break;
                case MainAppView.NEW_EXPENSE_PANEL:
                    form = new NewExpenseForm();
                    break;
                case MainAppView.EDIT_EXPENSE_PANEL:
                    if (expense != null) {
                        form = new NewExpenseForm(expense);
                    } else {
                        System.err.println(
                                "Erro: Tentativa de editar despesa sem fornecer objeto Expense");
                        return null;
                    }
                    break;
                case MainAppView.NEW_USER_PANEL:
                    form = new NewUserForm();
                    break;
                case MainAppView.EDIT_USER_PANEL:
                    if (user != null) {
                        form = new NewUserForm(user);
                    } else {
                        System.err.println(
                                "Erro: Tentativa de editar usuário sem fornecer objeto User");
                        return null;
                    }
                    break;
                default:
                    return null;
            }
            if (form != null) {
                return createPanelFromForm(form);
            }
            return null;
        } catch (Exception e) {
            System.err.println(
                    "Erro ao criar painel " + panelKey + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Creates a panel from a JFrame form.
     *
     * @param form The form to create a panel from
     * @return The created panel
     */
    private JPanel createPanelFromForm(JFrame form) {
        // Configura o formulário para não fechar a aplicação quando fechado
        form.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        // Força o layout a ser calculado antes de adicionar ao painel
        form.pack();
        // Obtém o contentPane do formulário
        Container contentPane = form.getContentPane();
        // Remove o contentPane do formulário para poder adicioná-lo ao nosso painel
        form.setContentPane(new javax.swing.JPanel());
        // Cria um novo painel com as dimensões corretas
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setPreferredSize(new Dimension(995,
                728));
        // Adiciona o conteúdo do formulário ao painel
        panel.add(contentPane,
                BorderLayout.CENTER);
        return panel;
    }
}
