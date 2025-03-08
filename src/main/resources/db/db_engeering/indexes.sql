USE sales_sync;

-- Create indexes for better performance
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