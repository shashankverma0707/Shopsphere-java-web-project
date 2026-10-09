CREATE DATABASE IF NOT EXISTS ecommerce_db;
USE ecommerce_db;

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    role ENUM('CUSTOMER','ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    price DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    category VARCHAR(80),
    image_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    status ENUM('PLACED','PROCESSING','SHIPPED','DELIVERED','CANCELLED') DEFAULT 'PLACED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    CONSTRAINT fk_items_product FOREIGN KEY (product_id) REFERENCES products(id)
);

CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_orders_user ON orders(user_id);

-- Register an account through the application, then promote it to admin with:
-- UPDATE users SET role='ADMIN' WHERE email='your-email@example.com';

INSERT INTO products(name,description,price,stock,category,image_url) VALUES
('Wireless Headphones','Noise-isolating wireless headphones',2499.00,25,'Electronics','https://via.placeholder.com/600x400?text=Headphones'),
('Mechanical Keyboard','RGB mechanical keyboard for productivity',3499.00,18,'Electronics','https://via.placeholder.com/600x400?text=Keyboard'),
('Smart Backpack','Water-resistant laptop backpack',1899.00,30,'Accessories','https://via.placeholder.com/600x400?text=Backpack'),
('USB-C Hub','7-in-1 USB-C multiport adapter',1599.00,40,'Accessories','https://via.placeholder.com/600x400?text=USB-C+Hub');
