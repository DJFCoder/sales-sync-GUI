USE sales_sync;

-- View for customer purchase history
-- Update the vw_customer_history view to include subtotal and discount
CREATE OR REPLACE VIEW vw_customer_history AS
SELECT 
    c.id AS customer_id,
    c.name AS customer_name,
    c.tax_id,
    'SALE' AS record_type,
    s.id AS record_id,
    s.date AS record_date,
    s.subtotal_amount,
    s.discount_amount,
    s.total_amount,
    s.payment_method,
    NULL AS status
FROM 
    customers c
JOIN 
    sales s ON c.id = s.customer_id
WHERE 
    s.canceled = FALSE
UNION ALL
SELECT 
    c.id AS customer_id,
    c.name AS customer_name,
    c.tax_id,
    'SERVICE' AS record_type,
    so.id AS record_id,
    so.creation_date AS record_date,
    NULL AS subtotal_amount,
    NULL AS discount_amount,
    NULL AS total_amount,
    NULL AS payment_method,
    so.status
FROM 
    customers c
JOIN 
    service_orders so ON c.id = so.customer_id;

-- View for daily sales report
CREATE OR REPLACE VIEW vw_daily_sales_report AS
SELECT 
    DATE(s.date) AS sale_date,
    COUNT(s.id) AS total_sales,
    SUM(s.total_amount) AS total_amount,
    s.payment_method,
    COUNT(DISTINCT s.customer_id) AS unique_customers
FROM 
    sales s
WHERE 
    s.canceled = FALSE
GROUP BY 
    DATE(s.date), s.payment_method;

-- View for daily expenses report
CREATE OR REPLACE VIEW vw_daily_expenses_report AS
SELECT 
    e.date AS expense_date,
    ec.name AS category_name,
    COUNT(e.id) AS total_expenses,
    SUM(e.amount) AS total_amount
FROM 
    expenses e
JOIN 
    expense_categories ec ON e.category_id = ec.id
GROUP BY 
    e.date, ec.name;

-- View for daily balance
CREATE OR REPLACE VIEW vw_daily_balance AS
SELECT 
    COALESCE(s.sale_date, e.expense_date) AS report_date,
    COALESCE(s.total_sales_amount, 0) AS total_sales,
    COALESCE(e.total_expenses_amount, 0) AS total_expenses,
    COALESCE(s.total_sales_amount, 0) - COALESCE(e.total_expenses_amount, 0) AS daily_balance
FROM 
    (SELECT 
        DATE(date) AS sale_date, 
        SUM(total_amount) AS total_sales_amount
     FROM 
        sales
     WHERE 
        canceled = FALSE
     GROUP BY 
        DATE(date)) s
FULL OUTER JOIN 
    (SELECT 
        date AS expense_date, 
        SUM(amount) AS total_expenses_amount
     FROM 
        expenses
     GROUP BY 
        date) e ON s.sale_date = e.expense_date;

-- View for service orders by status
CREATE OR REPLACE VIEW vw_service_orders_by_status AS
SELECT 
    so.status,
    COUNT(so.id) AS total_orders,
    MIN(so.creation_date) AS oldest_order_date,
    MAX(so.creation_date) AS newest_order_date,
    AVG(DATEDIFF(COALESCE(so.completion_date, CURRENT_DATE), so.creation_date)) AS avg_days_to_complete
FROM 
    service_orders so
GROUP BY 
    so.status;

-- View for monthly sales summary
CREATE OR REPLACE VIEW vw_monthly_sales_summary AS
SELECT 
    YEAR(s.date) AS year,
    MONTH(s.date) AS month,
    COUNT(s.id) AS total_sales,
    SUM(s.total_amount) AS total_amount,
    COUNT(DISTINCT s.customer_id) AS unique_customers,
    AVG(s.total_amount) AS average_sale_amount,
    MAX(s.total_amount) AS highest_sale_amount,
    MIN(s.total_amount) AS lowest_sale_amount
FROM 
    sales s
WHERE 
    s.canceled = FALSE
GROUP BY 
    YEAR(s.date), MONTH(s.date);

-- View for monthly expenses summary
CREATE OR REPLACE VIEW vw_monthly_expenses_summary AS
SELECT 
    YEAR(e.date) AS year,
    MONTH(e.date) AS month,
    ec.name AS category_name,
    COUNT(e.id) AS total_expenses,
    SUM(e.amount) AS total_amount,
    AVG(e.amount) AS average_expense_amount
FROM 
    expenses e
JOIN 
    expense_categories ec ON e.category_id = ec.id
GROUP BY 
    YEAR(e.date), MONTH(e.date), ec.name;

-- View for annual financial summary
CREATE OR REPLACE VIEW vw_annual_financial_summary AS
SELECT 
    COALESCE(s.year, e.year) AS report_year,
    COALESCE(s.total_sales_amount, 0) AS annual_sales,
    COALESCE(e.total_expenses_amount, 0) AS annual_expenses,
    COALESCE(s.total_sales_amount, 0) - COALESCE(e.total_expenses_amount, 0) AS annual_profit,
    CASE 
        WHEN COALESCE(e.total_expenses_amount, 0) > 0 
        THEN (COALESCE(s.total_sales_amount, 0) / COALESCE(e.total_expenses_amount, 1)) 
        ELSE NULL 
    END AS profit_ratio
FROM 
    (SELECT 
        YEAR(date) AS year, 
        SUM(total_amount) AS total_sales_amount
     FROM 
        sales
     WHERE 
        canceled = FALSE
     GROUP BY 
        YEAR(date)) s
FULL OUTER JOIN 
    (SELECT 
        YEAR(date) AS year, 
        SUM(amount) AS total_expenses_amount
     FROM 
        expenses
     GROUP BY 
        YEAR(date)) e ON s.year = e.year;

-- View for user activity log
CREATE OR REPLACE VIEW vw_user_activity_log AS
SELECT 
    u.id AS user_id,
    u.name AS user_name,
    u.login,
    u.type AS user_type,
    sl.date_time,
    sl.action,
    sl.details
FROM 
    system_logs sl
JOIN 
    users u ON sl.user_id = u.id
ORDER BY 
    sl.date_time DESC;

-- View for pending service orders
CREATE OR REPLACE VIEW vw_pending_service_orders AS
SELECT 
    so.id,
    c.name AS customer_name,
    c.phone AS customer_phone,
    so.description,
    so.creation_date,
    so.completion_date,
    DATEDIFF(so.completion_date, CURRENT_DATE) AS days_remaining,
    so.status
FROM 
    service_orders so
JOIN 
    customers c ON so.customer_id = c.id
WHERE 
    so.status IN ('PENDING', 'IN_PROGRESS')
ORDER BY 
    so.completion_date ASC;