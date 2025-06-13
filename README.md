# ✅ POS System – Spring Boot + MySQL

## 👥 Team Members

* **Srun Borath**
* **Sov YongKhang**
* **Sok Socheata**

---

## 🚀 Project Description

A simple Point of Sale (POS) backend system built with Spring Boot, MySQL, and REST APIs. Features include:

* Product & Inventory management
* Sale & Sale Item recording
* Invoice generation with PDF output
* Automatic inventory deduction after sales

---

## 📆 Technologies Used

* Java 17+
* Spring Boot
* Spring Data JPA
* MySQL
* iText (for PDF)
* Lombok

---

## 🛠️ How to Run the Project

### 🔹 Step 1: Clone the Repository

```bash
git clone https://github.com/Srunborath7/pos_system_java.git
cd pos_system_java
```

### 🔹 Step 2: Create the Database in MySQL

```sql
CREATE DATABASE pos_system;
```
```Create Table
CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255),
  email VARCHAR(255) UNIQUE,
  password VARCHAR(255),
  PRIMARY KEY (id)
);

CREATE TABLE category (
  id BIGINT NOT NULL AUTO_INCREMENT,
  cate_name VARCHAR(255),
  description VARCHAR(255),
  user_id BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT FK_category_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE customers (
  id BIGINT NOT NULL AUTO_INCREMENT,
  address VARCHAR(255),
  name VARCHAR(255),
  phone VARCHAR(255),
  user_id BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT FK_customers_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE product (
  id BIGINT NOT NULL AUTO_INCREMENT,
  description VARCHAR(255),
  price DOUBLE,
  product_name VARCHAR(255),
  category_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT FK_product_category FOREIGN KEY (category_id) REFERENCES category(id),
  CONSTRAINT FK_product_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE inventory (
  id BIGINT NOT NULL AUTO_INCREMENT,
  location VARCHAR(255),
  quantity INT NOT NULL,
  product_id BIGINT,
  user_id BIGINT,
  PRIMARY KEY (id),
  CONSTRAINT FK_inventory_product FOREIGN KEY (product_id) REFERENCES product(id),
  CONSTRAINT FK_inventory_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE sale (
  sale_id BIGINT NOT NULL AUTO_INCREMENT,
  description VARCHAR(255),
  sale_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  customer_id BIGINT,
  user_id BIGINT NOT NULL,
  PRIMARY KEY (sale_id),
  CONSTRAINT FK_sale_customer FOREIGN KEY (customer_id) REFERENCES customers(id),
  CONSTRAINT FK_sale_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE sale_item (
  sale_item_id BIGINT NOT NULL AUTO_INCREMENT,
  discount DOUBLE,
  quantity INT,
  sale_date DATETIME(6),
  total DOUBLE,
  product_id BIGINT NOT NULL,
  sale_id BIGINT NOT NULL,
  PRIMARY KEY (sale_item_id),
  CONSTRAINT FK_sale_item_product FOREIGN KEY (product_id) REFERENCES product(id),
  CONSTRAINT FK_sale_item_sale FOREIGN KEY (sale_id) REFERENCES sale(sale_id)
);

CREATE TABLE invoice (
  invoice_id BIGINT NOT NULL AUTO_INCREMENT,
  sale_id BIGINT NOT NULL,
  customer_cash DOUBLE NOT NULL,
  return_cash DOUBLE NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (invoice_id),
  CONSTRAINT FK_invoice_sale FOREIGN KEY (sale_id) REFERENCES sale(sale_id) ON DELETE CASCADE
);
```
### 🔹 Step 3: Configure Application Properties

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/pos_system
spring.datasource.username=root
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

### 🔹 Step 4: Run the Application

#### ✅ Option A: Terminal

```bash
./mvnw spring-boot:run
# or
mvn spring-boot:run
```

#### ✅ Option B: In IntelliJ or Eclipse

* Open the project.
* Run `PosSystemApplication.java`.

Once running, the server will be available at:

```
http://localhost:8080
```

---

## 📬 Using Postman to Test the API

### 🌐 Base URL

```
http://localhost:8080/api/
```

---

### 🔹 1. Add Customer

**POST** `/customers`

```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "phone": "012345678"
}
```

### 🔹 2. Add User (Cashier)

**POST** `/users`

```json
{
  "name": "Cashier 1",
  "email": "cashier1@example.com",
  "password": "123456"
}
```

### 🔹 3. Add Product

**POST** `/products`

```json
{
  "productName": "Coca Cola",
  "price": 1.5,
  "description": "Soft drink"
}
```

### 🔹 4. Add Inventory

**POST** `/inventories`

```json
{
  "quantity": 50,
  "location": "Main Store",
  "product": { "id": 1 },
  "user": { "id": 1 }
}
```

### 🔹 5. Create Sale

**POST** `/sales`

```json
{
  "description": "Customer Purchase",
  "user": { "id": 1 },
  "customer": { "id": 1 }
}
```

### 🔹 6. Add Sale Items

**POST** `/sale-items`

```json
{
  "sale": { "saleId": 1 },
  "product": { "id": 1 },
  "quantity": 3,
  "discount": 10
}
```

✔ Inventory is automatically updated when this is posted.

### 🔹 7. Generate Invoice PDF

**POST** `/invoices`
🧃 **Set Header**: `Accept: application/pdf`

**Body:**

```json
{
  "saleId": 1,
  "customerCash": 10.0
}
```

📂 The PDF will download and be saved in the `invoices/` folder.

### 🔹 8. Get All Invoices

**GET** `/invoices`

### 🔹 9. Get Invoice by ID

**GET** `/invoices/{id}`

---

## 📮 Postman Collection

You can import the collection directly into Postman to test the API.

📎 Download here: [Pos_System.postman_collection.json](./Pos_System.postman_collection.json)

To import:

1. Open Postman
2. Click `Import`
3. Upload the file `Pos_System.postman_collection.json`
4. Test all endpoints instantly!

---

## 📁 Invoice Output

Invoices are saved in:

```
/invoices/invoice_sale_{saleId}.pdf
```

---

## 📙 License

This project is for academic and educational purposes.

> Feel free to fork or contribute!

---

## 💾 GitHub Push Instructions

```bash
# If not already a Git repo
git init

git remote add origin https://github.com/Srunborath7/pos_system_java.git

git add .
git commit -m "📝 Add complete README and finalize project setup"
git push -u origin main
```


