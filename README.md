# ecommerce-backend
# 🛍️ BargainBay - Backend (Spring Boot + MongoDB)

This repository contains the backend of the BargainBay e-commerce web application.  
It is built using **Spring Boot** and **MongoDB**, following a secure and scalable REST API architecture.

## 🔑 Key Features
- User registration & login with **JWT authentication**
- **Role-based access control** (User / Admin)
- **Product management** (Add, update, delete, list – Admin only)
- **Shopping & order APIs** for customers
- Secured endpoints via **Spring Security**
- CORS enabled for communication with React frontend

## 🧰 Tech Stack
| Technology | Purpose |
|----------|---------|
| Java 17 | Primary language |
| Spring Boot | REST APIs & business logic |
| MongoDB | Database |
| Spring Security + JWT | Authentication & authorization |
| Maven | Dependency management |

## 🚀 How It Works
- Admin can manage products via protected endpoints
- Users can authenticate, browse products, add to cart, and place orders
- JWT is generated on login and passed in the Authorization header for secure API access

This backend powers the complete e-commerce workflow for the BargainBay platform.
