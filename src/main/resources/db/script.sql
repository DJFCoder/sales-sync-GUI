-- Drop existing database if it exists
DROP DATABASE IF EXISTS sales_sync;

-- Create SalesSync Database
CREATE DATABASE IF NOT EXISTS sales_sync;
USE sales_sync;

-- Drop tables if they exist to avoid conflicts
DROP TABLE IF EXISTS sale_items;
DROP TABLE IF EXISTS sales;
DROP TABLE IF EXISTS service_orders;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS expense_categories;
DROP TABLE IF EXISTS system_logs;
DROP TABLE IF EXISTS user_activities;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS user_types;

-- Create user_types table
CREATE TABLE user_types (
    id INT PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE
);

-- Create users table
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    login VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    user_type_id INT NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_type_id) REFERENCES user_types(id)
);

-- Create customers table
CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    tax_id VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100),
    phone VARCHAR(20),
    address VARCHAR(255),
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create expense_categories table
CREATE TABLE expense_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Create expenses table
CREATE TABLE expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    description VARCHAR(255) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    date DATE NOT NULL,
    category_id INT,
    recurrence_type ENUM('DAILY', 'WEEKLY', 'MONTHLY', 'ANNUAL'),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES expense_categories(id)
);

-- Create sales table
CREATE TABLE sales (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    user_id INT NOT NULL,
    date DATETIME NOT NULL,
    payment_method ENUM('CASH', 'CREDIT_CARD', 'DEBIT_CARD', 'BANK_TRANSFER', 'PIX', 'BANK_SLIP', 'PAYCHECK'),
    payment_date DATETIME,
    subtotal_amount DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0.00,
    total_amount DECIMAL(10,2) NOT NULL,
    canceled BOOLEAN DEFAULT FALSE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create sale_items table
CREATE TABLE sale_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sale_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE CASCADE
);

-- Create service_orders table
CREATE TABLE service_orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    description TEXT NOT NULL,
    creation_date DATE NOT NULL,
    completion_date DATE,
    status ENUM('PENDING', 'IN_PROGRESS', 'COMPLETED', 'CANCELED') DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

-- Create system_logs table
CREATE TABLE system_logs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    date_time DATETIME NOT NULL,
    action VARCHAR(100) NOT NULL,
    details TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create user_activities table
CREATE TABLE user_activities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    activity_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Create indexes for better performance
CREATE INDEX idx_customers_name ON customers(name);
CREATE INDEX idx_sales_customer ON sales(customer_id);
CREATE INDEX idx_sales_user ON sales(user_id);
CREATE INDEX idx_sales_date ON sales(date);
CREATE INDEX idx_sale_items_sale ON sale_items(sale_id);
CREATE INDEX idx_service_orders_customer ON service_orders(customer_id);
CREATE INDEX idx_service_orders_status ON service_orders(status);
CREATE INDEX idx_expenses_category ON expenses(category_id);
CREATE INDEX idx_expenses_date ON expenses(date);
CREATE INDEX idx_system_logs_user ON system_logs(user_id);
CREATE INDEX idx_system_logs_datetime ON system_logs(date_time);
CREATE INDEX idx_user_activities_user ON user_activities(user_id);
CREATE INDEX idx_user_activities_time ON user_activities(activity_time);

-- Additional indexes for better query performance
CREATE INDEX idx_customers_tax_id ON customers(tax_id);
CREATE INDEX idx_customers_email ON customers(email);
CREATE INDEX idx_customers_phone ON customers(phone);
CREATE INDEX idx_users_login ON users(login);
CREATE INDEX idx_users_type_id ON users(user_type_id);
CREATE INDEX idx_user_types_name ON user_types(name);
CREATE INDEX idx_expense_categories_name ON expense_categories(name);
CREATE INDEX idx_sales_payment_method ON sales(payment_method);
CREATE INDEX idx_sales_payment_date ON sales(payment_date);
CREATE INDEX idx_service_orders_creation_date ON service_orders(creation_date);

-- Create views for common queries

-- View for customer purchase history
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
    ut.name AS user_type,
    sl.date_time,
    sl.action,
    sl.details
FROM 
    system_logs sl
JOIN 
    users u ON sl.user_id = u.id
JOIN
    user_types ut ON u.user_type_id = ut.id
ORDER BY 
    sl.date_time DESC;

-- View for recent user activities
CREATE OR REPLACE VIEW vw_recent_user_activities AS
SELECT 
    ua.id,
    u.id AS user_id,
    u.name AS user_name,
    ua.description,
    ua.activity_time,
    DATE_FORMAT(ua.activity_time, '%d/%m/%Y %H:%i') AS formatted_time,
    DATE_FORMAT(ua.activity_time, '%H:%i') AS formatted_time_short
FROM 
    user_activities ua
JOIN 
    users u ON ua.user_id = u.id
ORDER BY 
    ua.activity_time DESC;

-- View for pending service orders
CREATE OR REPLACE VIEW vw_pending_service_orders AS
SELECT 
    so.id,
    c.name AS customer_name,
    c.phone AS customer_phone,
    so.description,
    so.creation_date,
    so.completion_date,
    DATEDIFF(COALESCE(so.completion_date, CURRENT_DATE), CURRENT_DATE) AS days_remaining,
    so.status
FROM 
    service_orders so
JOIN 
    customers c ON so.customer_id = c.id
WHERE 
    so.status IN ('PENDING', 'IN_PROGRESS')
ORDER BY 
    so.completion_date ASC;