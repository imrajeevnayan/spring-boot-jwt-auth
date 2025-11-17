# Simple JWT Authentication Project

[![Java](https://img.shields.io/badge/Java-17%2B-blue)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.3-green)](https://spring.io/projects/spring-boot)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![H2 Database](https://img.shields.io/badge/Database-H2-orange)](https://www.h2database.com/html/main.html)
[![Tailwind CSS](https://img.shields.io/badge/UI-Tailwind%20CSS-blueviolet)](https://tailwindcss.com/)

A full-stack, secure authentication system built with **Spring Boot**, featuring **JWT tokens**, **email verification**, **password reset**, and **OAuth2 social logins** (Google & GitHub). The frontend uses **Thymeleaf** with **responsive Tailwind CSS** for modern, mobile-friendly UI. Powered by **H2 in-memory DB** for quick prototyping (easy swap to MySQL/PostgreSQL).

This project demonstrates best practices for stateless auth in microservices/APIs, with extensible code for production. Includes user-friendly pages for **login**, **register**, and **dashboard** with seamless flows.

## ✨ Features
- **JWT Authentication**: Stateless tokens (1-hour expiry) for secure API access.
- **User Registration**: Flexible fields (firstName, lastName, email, password); auto-roles ("USER").
- **Email Verification**: Post-registration email with one-time link (using Spring Mail/Gmail).
- **Password Reset**: "Forgot Password" flow with expiring tokens (1-hour).
- **OAuth2 Social Login**: One-click sign-in with **Google** & **GitHub** (auto-saves users to DB).
- **Protected Dashboard**: Role-based access; displays user data post-login.
- **Responsive UI**: Tailwind CSS—stacks on mobile, grids on desktop/tablet.
- **Security**: BCrypt hashing, CSRF disabled (enable in prod), verified-only logins.
- **Database**: H2 (embedded)—view data via console; JPA auto-schema.

## 📱 Screenshots
### Register Page
A clean form for new users with responsive fields (name, email, password). Mobile: Stacked; Desktop: Grid layout.

![Register Page](https://via.placeholder.com/800x600?text=Register+Page)  
*(Screenshot: Form with Tailwind styling—first/last name side-by-side on larger screens.)*

### Login Page
Includes email/password form, "Forgot Password" link, and social buttons (Google/GitHub). Errors show in red banners.

![Login Page](https://via.placeholder.com/800x600?text=Login+Page)  
*(Screenshot: Social buttons stack on mobile; error message for invalid creds.)*

### Dashboard Page
Protected view showing welcome message, user email/full name, and logout button. Responsive cards for data.

![Dashboard Page](https://via.placeholder.com/800x600?text=Dashboard+Page)  
*(Screenshot: Personalized greeting—"Welcome, John Doe!" with email; logout button aligned right on desktop.)*

*(Add real screenshots to GitHub repo by uploading to `docs/` folder and updating links.)*

## 🛠 Prerequisites
- **Java 17+** (JDK installed; use SDKMAN or Oracle/OpenJDK).
- **Maven 3.6+** (for build/run).
- **IDE**: IntelliJ IDEA, VS Code, or Eclipse (optional, but recommended for Thymeleaf previews).
- **Gmail Account** (for email testing; enable 2FA + generate app password—see guide below).
- **Google & GitHub OAuth Apps** (see Configuration below).

No Node.js or frontend build tools—Tailwind via CDN for simplicity.

## 🚀 Quick Start
Follow these steps to get running in <10 minutes.

### 1. Generate Base Project from Spring Initializr
- Visit [start.spring.io](https://start.spring.io).
- **Project**: Maven Project.
- **Language**: Java.
- **Spring Boot**: 3.3.3 (latest stable).
- **Group**: `com.example`.
- **Artifact**: `simple-auth`.
- **Name**: `simple-auth`.
- **Package name**: `com.example.simpleauth`.
- **Packaging**: Jar.
- **Java**: 17.
- **Dependencies**: Add these (search & select):
    - Spring Web
    - Spring Security
    - Spring Data JPA
    - H2 Database
    - Thymeleaf
    - Validation
    - Spring Boot Starter Mail (for emails)
    - OAuth2 Client (for Google/GitHub)
- Click **Generate** → Download ZIP → Extract to a folder named `simple-auth-project`.

### 2. Add Project Files
- Open the extracted folder in your IDE (e.g., IntelliJ: Open → select folder).
- **Replace/Add Files**: Copy the complete code from the [Final Code Guide](link-to-conversation-or-repo) into the corresponding paths.
    - `pom.xml`: Update with JJWT dependencies.
    - Java classes: In `src/main/java/com/example/simpleauth/` (controllers, entities, etc.).
    - Properties: `src/main/resources/application.properties`.
    - Templates: `src/main/resources/templates/` (login.html, register.html, dashboard.html, etc.).
    - Static: `src/main/resources/static/js/app.js` (JS helpers).
- **Folder Structure Overview** (after adding):
  ```
  simple-auth-project/
  ├── pom.xml
  ├── src/
  │   └── main/
  │       ├── java/com/example/simpleauth/
  │       │   ├── SimpleAuthApplication.java
  │       │   ├── config/ (SecurityConfig.java, PasswordConfig.java, etc.)
  │       │   ├── controller/ (AuthController.java, DashboardController.java, etc.)
  │       │   ├── entity/ (User.java)
  │       │   ├── repository/ (UserRepository.java)
  │       │   ├── service/ (UserService.java)
  │       │   ├── util/ (JwtUtils.java)
  │       │   └── mail/ (EmailService.java)
  │       └── resources/
  │           ├── application.properties
  │           ├── static/js/app.js
  │           └── templates/ (login.html, register.html, dashboard.html, etc.)
  └── README.md
  ```

### 3. Configure Secrets (Critical for Security)
Edit `src/main/resources/application.properties` (never commit real secrets—use `.gitignore`):
```properties
# H2 Database (In-Memory)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true  # Access: http://localhost:8080/h2-console

# Thymeleaf (Dev)
spring.thymeleaf.cache=false

# JWT
app.jwtSecret=${JWT_SECRET:dev-secret-only}  # Use env var in prod
app.baseUrl=http://localhost:8080

# Mail (Gmail - App Password Required)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${MAIL_USERNAME:your-gmail@gmail.com}
spring.mail.password=${MAIL_PASSWORD:your-app-password}
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# Google OAuth
spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID:mock}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET:mock}
spring.security.oauth2.client.registration.google.scope=openid,email,profile

# GitHub OAuth
spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID:mock}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET:mock}
spring.security.oauth2.client.registration.github.scope=user:email
spring.security.oauth2.client.provider.github.user-name-attribute=login

# Logging
logging.level.org.springframework.security=DEBUG
logging.level.org.springframework.mail=DEBUG
```

- **Env Vars Setup**: In IDE (IntelliJ): Run Config → Modify → Environment Variables → Add keys/values.
- **Gmail App Password**: Follow [this guide](link-to-previous-response) to generate (required for SMTP).
- **OAuth Creds**: From Google/GitHub consoles (see Usage below).

### 4. Build & Run
- Terminal in project root: `mvn clean install` (downloads deps).
- Start: `mvn spring-boot:run`.
- App: `http://localhost:8080` (redirects to `/login`).
- H2 Console: `http://localhost:8080/h2-console` (JDBC: `jdbc:h2:mem:testdb`; view users).

## 📖 Usage: Step-by-Step Testing
Test the full flow—each feature is independent but integrated.

### 1. Register a New User
- Go to `http://localhost:8080/register`.
- Fill: First Name (John), Last Name (Doe), Email (john@example.com), Password (123456).
- Submit → Alert: "Registered! Check email to verify."
- **Backend**: User saved unverified; token + email sent.

### 2. Verify Email
- Check inbox/spam for "Verify Your Email" (link: `http://localhost:8080/verify?token=...`).
- Click → Redirect to `/verify-success`: "Email Verified! Go to Login."
- **Backend**: Token validated; user marked verified.

### 3. Login with Email/Password
- Go to `http://localhost:8080/login`.
- Enter email/password → Submit.
- Success: Token in localStorage → Redirect to `/dashboard`.
- **Errors**: Invalid → Red banner. Unverified → "Email not verified."

### 4. Login with OAuth2 (Google/GitHub)
- On `/login`, click "Sign in with Google" or "Sign in with GitHub."
- Auth on provider → Redirect back → Auto-JWT + dashboard.
- **First Time**: User auto-created (email from provider, verified=true).

### 5. Password Reset
- On `/login`, click "Forgot Password?" → `/forgot-password`.
- Enter email → Submit → "Reset email sent!"
- Click link in email → `/reset-password` → New password → Redirect to login.

### 6. Access Dashboard
- Post-login: `/dashboard` shows "Welcome, [Full Name]!" + email + logout.
- **Protected**: Unauthorized → Back to login.

## 🏗 Project Structure
- **Java Packages**:
    - `config/`: Security, web configs (JWT filter, OAuth).
    - `controller/`: Handles routes (auth, dashboard, verify, forgot/reset).
    - `entity/`: `User.java` (implements UserDetails + OAuth2User).
    - `repository/`: `UserRepository.java` (JPA queries).
    - `service/`: `UserService.java` (load/save, tokens, OAuth save).
    - `util/`: `JwtUtils.java` (token gen/validate).
    - `mail/`: `EmailService.java` (verification/reset emails).
- **Resources**:
    - `application.properties`: All configs (externalize secrets!).
    - `templates/`: Thymeleaf HTML (login.html, etc.) with Tailwind.
    - `static/js/app.js`: Form validation, token storage (fetch API).

## 🔧 Customization
- **Add User Fields**: Extend `User` (e.g., `phone`), update forms/repo/service.
- **Real Database**: Change `spring.datasource.url` to MySQL; add dep.
- **More OAuth**: Add Facebook in properties + button in login.html.
- **Refresh Tokens**: Extend `JwtUtils` with renewal endpoint.
- **Production**:
    - Enable CSRF: In SecurityConfig.
    - HTTPS: Use Nginx proxy.
    - Secrets: Env vars or AWS Secrets Manager.
    - Tailwind Build: npm install tailwindcss → Build CSS for offline.

## 🐛 Troubleshooting
| Issue | Solution |
|-------|----------|
| **Build Fails** | `mvn clean install`; check Java 17. |
| **Port Busy** | `server.port=8081` in properties. |
| **No Email** | Check Gmail App Password (guide above); add `logging.level.org.springframework.mail=DEBUG`. |
| **OAuth Error** | Verify URIs in Google/GitHub console; add test users. |
| **JWT Invalid** | Regenerate secret; check localStorage (F12). |
| **H2 Missing** | `spring.jpa.hibernate.ddl-auto=update`; restart. |

**Logs**: Console shows DEBUG—search "Security" or "Mail".

## 📄 License
MIT—free to use/modify. See [LICENSE](LICENSE).

## 🤝 Contributing
Fork → Changes → PR. Issues welcome!

Built with ❤️ using Spring Boot (Nov 2025). Questions? Check comments or issues.

