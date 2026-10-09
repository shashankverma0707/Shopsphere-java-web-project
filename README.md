# ShopSphere â€” Scalable Online E-Commerce Platform

A college Java web application demonstrating Java OOP, Servlets/JSP, JDBC, MySQL, collections/generics, exception handling, multithreading, synchronization, customer authentication, shopping cart, checkout, order history and admin inventory.

## Stack
- Java 17+ / Maven
- Jakarta Servlet 6 and JSP 3.1
- Apache Tomcat 10.1+ (Tomcat 9 is not compatible with `jakarta.*`)
- MySQL 8+ / JDBC / PreparedStatement
- BCrypt password hashing
- JSP/JSTL, HTML and responsive CSS

## Features
- Product listing and search
- Customer registration and login; password hashes are stored, not plaintext passwords
- Session-based shopping cart
- Checkout creates order and order-item rows in a JDBC transaction, locks product rows with `SELECT ... FOR UPDATE`, checks stock, and rolls back on failure
- Customer-specific order history
- Admin product creation, update-capable DAO, and product deactivation/deletion interface
- `User` inheritance (`Customer`, `Admin`) and polymorphic role behavior
- `CrudDAO<T>` generic interface, `List<Product>` and `Map<Integer,CartItem>`
- `DiscountStrategy` interface and implementations
- `ExecutorService` background order-processing demonstration
- `synchronized` stock-reservation demonstration; database locks remain the real checkout concurrency protection

## Setup
### 1. Install requirements
Install JDK 17+, Maven 3.8+, MySQL 8+, and Tomcat 10.1+. VS Code extensions: Extension Pack for Java and Maven for Java.

### 2. Create database
Run `database/schema.sql` in MySQL Workbench or the MySQL CLI. It creates `ecommerce_db` and sample products.

### 3. Configure DB credentials
Set environment variables (do not commit your real password).

**Windows PowerShell**
```powershell
$env:ECOM_DB_URL="jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC"
$env:ECOM_DB_USER="root"
$env:ECOM_DB_PASSWORD="YOUR_MYSQL_PASSWORD"
```
**macOS/Linux**
```bash
export ECOM_DB_URL='jdbc:mysql://localhost:3306/ecommerce_db?useSSL=false&serverTimezone=UTC'
export ECOM_DB_USER='root'
export ECOM_DB_PASSWORD='YOUR_MYSQL_PASSWORD'
```
`.env.example` is a template only; Java does not automatically load `.env`.

### 4. Build and deploy
From the project root:
```bash
mvn clean package
```
Copy `target/shopsphere.war` to Tomcat's `webapps/` folder, start Tomcat, and visit `http://localhost:8080/shopsphere/products`.

### 5. Create an admin account
1. Register a normal account at `/shopsphere/register`.
2. In MySQL, run `UPDATE users SET role='ADMIN' WHERE email='your-email@example.com';` with the account email.
3. Log out and back in; then open `/shopsphere/admin/products`.

## Project structure
```text
database/schema.sql
src/main/java/com/ecommerce/
  dao/        CrudDAO, DbConnection, ProductDAO, UserDAO, OrderDAO
  exception/  AppException, InsufficientStockException
  model/      User, Customer, Admin, Product, CartItem, Order
  service/    ProductService, OrderService, DiscountStrategy, StockManager, OrderProcessingService
  servlet/    ProductList, Register, Login, Logout, Cart, AddToCart, Checkout, Orders, AdminProduct
  util/       PasswordUtil
src/main/webapp/WEB-INF/views/  JSP pages
src/main/webapp/css/style.css
```

## GitHub + VS Code
1. Create a public GitHub repository named `shopsphere-java-web-project` (leave README initialization unchecked).
2. Open the extracted project folder in VS Code. In **Terminal â†’ New Terminal**, run:
```bash
git init
git add .
git commit -m "Build ShopSphere Java e-commerce project"
git branch -M main
git remote add origin https://github.com/YOUR_USERNAME/shopsphere-java-web-project.git
git push -u origin main
```
3. Do not push `target/`, real credentials, `.env`, or private data. `.gitignore` excludes common generated files.

## Rubric mapping
- **Problem understanding/design:** layered Servlet â†’ service â†’ DAO â†’ JDBC architecture; customer/admin workflows and relational schema.
- **Core Java/OOP:** abstract `User`, inheritance, overridden methods, interfaces, encapsulation, custom exceptions.
- **Collections/generics:** `Map<Integer,CartItem>`, `List<Product>`, generic `CrudDAO<T>`.
- **Multithreading/synchronization:** `ExecutorService` background worker and synchronized `StockManager`; JDBC row locks prevent overselling at checkout.
- **Database/JDBC:** DAO classes, PreparedStatement, ResultSet, generated keys, transaction/rollback.
- **Servlets/web integration:** product search, login/register/logout, cart, checkout, order history, admin inventory.

## Smoke-test checklist
- [ ] Product page loads and search filters results
- [ ] Register a new customer and log in
- [ ] Incorrect password is rejected
- [ ] Add product to cart and verify stock limit
- [ ] Guest checkout redirects to login
- [ ] Checkout writes order and order items and reduces stock
- [ ] Insufficient stock causes rollback
- [ ] Customer cannot access admin inventory
- [ ] Admin can add/deactivate a product
- [ ] Order history only shows the signed-in user's orders

## Important scope note
This is an academic project foundation, not production-ready commerce software. Before real deployment, add CSRF tokens, rate limiting, HTTPS/secure cookie configuration, audit logging, centralized error handling, and payment integration. Only mark tests as passed after running them locally.
