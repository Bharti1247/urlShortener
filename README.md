# 🔗 URL Shortener – Spring Boot Backend

A production-grade **URL Shortener backend** built using **Spring Boot**, featuring:
- URL shortening & redirection
- Role-based security (ADMIN / USER / OPS)
- Enable/Disable short URLs
- Audit trail
- Centralized logging with MDC
- In-memory caching
- Custom JSON error handling

This project is designed with **enterprise best practices** and is suitable for **real-world usage and interviews**.

---

## 🚀 Features

### Core Functionality
- Create or retrieve short URL for a given original URL
- Redirect short URL → original URL (HTTP **303 See Other**)
- Hit count tracking
- Enable / Disable short URLs

### Security
- Spring Security with **DB-backed users**
- HTTP Basic authentication
- Role-based authorization
  - `ADMIN` → enable/disable short URLs
  - Public access for redirection
- Custom **401 / 403 JSON responses**

### Audit Trail
- Tracks:
  - ENABLE / DISABLE actions
  - Who performed the action
  - Previous and new state
  - Timestamp

### Caching
- In-memory caching using Spring Cache abstraction
- Cache eviction on status change
- Reduces DB hits for URL resolution

### Logging
- Centralized logging with **SLF4J**
- MDC-based request tracing:
  - requestId
  - user
  - IP address
  - HTTP method
  - request path
- Structured, production-grade logs

### Exception Handling
- Global exception handler
- Consistent JSON error responses
- Proper HTTP status codes:
  - 404 – Not Found
  - 410 – Gone (Disabled URL)
  - 401 – Unauthorized
  - 403 – Forbidden

---

## 🏗️ Tech Stack

| Layer | Technology |
|-----|-----------|
| Language | Java 17 |
| Framework | Spring Boot |
| Security | Spring Security |
| ORM | Spring Data JPA |
| Database | MySQL |
| Caching | Spring Cache (ConcurrentMapCache) |
| Logging | SLF4J + Logback |
| Build Tool | Maven |
| JSON | Jackson |
| Validation | Jakarta Validation |

---

## 🗄️ Database Setup

This application uses **MySQL** as the primary data store.

### Database
- Create the database manually:
```sql
CREATE DATABASE link_shortener;
```
- Update application.properties accordingly.

### 🧱 Database Tables
- Core Tables
  - users – application users
  - roles – security roles
  - user_roles – user–role mapping
  - url_mapping – short URL data
  - url_audit – audit trail for enable/disable actions
 
- Key Fields
  - url_mapping
    | Column              | Description           |
    | ------------------- | --------------------- |
    | `id`                | Primary key           |
    | `original_url`      | Original long URL     |
    | `short_code`        | Generated short code  |
    | `short_url_enabled` | Enable / disable flag |
    | `hit_count`         | Number of redirects   |
    | `created_at`        | Creation timestamp    |

  - url_audit
    | Column           | Description                   |
    | ---------------- | ----------------------------- |
    | `id`             | Primary key                   |
    | `short_code`     | Short URL identifier          |
    | `action`         | ENABLE / DISABLE              |
    | `performed_by`   | User who performed the action |
    | `previous_state` | Previous enable state         |
    | `new_state`      | New enable state              |
    | `performed_at`   | Action timestamp              |

---

## 🔐 Security Rules

Role-based access control is enforced using Spring Security.
| Endpoint              | Method | Access        |
| --------------------- | ------ | ------------- |
| `/{shortCode}`        | GET    | Public        |
| `/{shortCode}/status` | PATCH  | ADMIN only    |
| `/api/**`             | Any    | Authenticated |

### Authentication mechanism:
- HTTP Basic Authentication
- DB-backed users
- BCrypt password encoding

---

## 📡 API Endpoints



---
## 🧪 Testing via Postman

- Select Authorization → Basic Auth
- Use credentials from data.sql
- ADMIN role is required for PATCH requests
- Redirect endpoint is publicly accessible

---

## 📊 Logging & Monitoring

Centralized logging is implemented with:
- SLF4J + Logback
- MDC context for:
  - requestId
  - user
  - IP address
  - HTTP method
  - request path

---

## 🧠 Design Highlights

- Clean layered architecture
- Stateless REST APIs
- Centralized exception handling
- Role-based security
- Audit trail for critical operations
- Cache-aware service design
- Production-grade logging

---

## 👨‍💻 Author

- Bharti
- Backend Engineer – Java & Spring Boot

---
