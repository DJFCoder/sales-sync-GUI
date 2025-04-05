USE sales_sync;

-- Insert user types
INSERT INTO user_types (id, name) VALUES
(0, 'ADMIN'),
(1, 'OWNER'),
(2, 'EMPLOYEE');

-- Create default admin user (password: @devjf123admin)
INSERT INTO users (name, login, password, user_type_id) VALUES (
    'Dev_JF', 'admin', '$2a$12$hVYhdhpdB0QYdu/TJJia/e4yboPMav1s9YFLNoRfBVwYtO6oPex1.', 0
    );

-- Insert default expense categories
INSERT INTO expense_categories (name, description) VALUES 
('Utilidades', 'Eletricidade, água, internet, etc.'),
('Aluguel', 'Aluguel de escritório ou loja'),
('Salários', 'Salários dos funcionários'),
('Suprimentos', 'Material de escritório e suprimentos'),
('Marketing', 'Despesas com publicidade e marketing');

-- Insert test customer
INSERT INTO customers (name, tax_id) VALUES (
    "Cliente Teste",
    "123.123.123-12"
    );
