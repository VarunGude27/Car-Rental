# 🚗 Car Rental Web Platform

A full-stack car rental website built using **Spring Boot**, **React.js**, **MySQL**, and **JWT** for authentication. This system streamlines the rental process for customers, agencies, and administrators with secure role-based access and a responsive user interface.

<img
align="right"
src="https://visitor-badge.laobi.icu/badge?page_id=Car_Rental_Spring-boot_React"
/>

---

## 🖼️ Screenshots

### 🔧 Dashboard View

![Dashboard](https://github.com/user-attachments/assets/3509ee9e-9057-404d-8da2-853cfc8ae351)


### 🌐 Home Page

![Homepage](https://github.com/user-attachments/assets/073cb8d3-b7a9-4ebb-808e-8ca8df2d701d)

### 📊 User Flowchart

![User Flowchart](https://github.com/user-attachments/assets/e6efec26-0056-411a-9ed3-5737e51a563e)

---

## 📘 Documentation

### 🌟 Project Objectives

* Simplify car rental operations for customers and agencies.
* Enable agencies to manage listings and bookings.
* Allow admins to manage the platform with analytics and user verification.
* Provide a responsive, secure, and user-friendly interface.

---

## 👥 User Roles & Features

### 1️⃣ **Customer**

* View, filter, and search for available cars.
* Reserve a car with date, location, and driver options.
* View rental history and order status.
* Rate rented vehicles and view average ratings.
* Submit NRC image for verification.

### 2️⃣ **Agency**

* CRUD operations on car listings with up to 4 images.
* View and manage car availability and bookings.
* Approve or deny rental orders.
* Submit verification (NRC/license) to admin.

### 3️⃣ **Administrator**

* Verify or suspend user accounts.
* View user and rental statistics.
* Manage customer feedback and agency requests.

---

## 📦 Core Features

### 🚗 Car Management

* Add, update, and delete cars with multiple images.
* Filter cars by brand, category, price, and model.
* Auto-check car availability to prevent double bookings.

### 📁 Rental Orders

* Customers create rental orders; agencies approve or deny.
* Status tracking: `Pending`, `Approved`, `Denied`, `Cancelled`.
* Rental history view for both customers and agencies.

### ⭐ Ratings & Feedback

* Customers rate cars post-rental.
* View average ratings on car detail pages.
* Submit general feedback; admin can manage responses.

### 🔐 Authentication & Authorization

* Secure login with JWT tokens.
* Role-based access (Customer, Agency, Admin).
* Multi-step registration (Customer/Agency with document upload).

---

## ⚙️ System Requirements

### ✅ Functional Requirements

* Three user roles (Admin, Customer, Agency).
* Car listing and search system.
* Rental order system with status handling.
* Role-specific dashboards and permissions.
* Responsive design for all screen sizes.

### 📈 Non-Functional Requirements

* Page load ≤ 3 seconds.
* Scalable to thousands of concurrent users.
* JWT-based auth, HTTPS, and encrypted passwords.
* Optimized database queries for performance.

---

## 🧹 Tech Stack

| Layer      | Technology                           |
| ---------- | ------------------------------------ |
| Frontend   | React.js + Tailwind CSS              |
| Backend    | Spring Boot (Java)                   |
| Database   | MySQL                                |
| Auth       | JWT (JSON Web Token)                 |
| Deployment | Netlify (Frontend), Render (Backend) |

---

## 🛠️ Setup & Installation

### Backend (Spring Boot)

```bash
cd backend
./mvnw clean install
./mvnw spring-boot:run
```

* Runs at: `http://localhost:8000`

### Frontend (React.js with Vite)

```bash
cd frontend
npm install
npm run dev
```

* Runs at: `http://localhost:5173`

### MySQL

* Create DB: `car_rental`
* Import schema from: `src/main/resources/schema.sql`

---

## 📁 Folder Structure

```bash
🔽️ backend
├── src/main/java/com/rental
│   ├── controller
│   ├── service
│   ├── entity
│   └── repository
🔽️ frontend
    ├── components
    ├── pages
    ├── routes
    └── api
```

---

## 📄 License

This project is open-source and available under the [MIT License](LICENSE).

---

## ✍️ Author

**Myat Thura Soe**
📧 [LinkedIn](https://www.linkedin.com/in/myatthurasoe) | 📨 [Email Me](mailto:your.email@example.com)

---

> Feel free to fork this repository, submit PRs, and contribute! 🚀


