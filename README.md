🎉 Festival Management Platform

Full-stack Java · Spring Boot · MVC Architecture · Security · MySQL

A full-featured festival & event management platform built using Java/Spring during a 6-month K-Digital Training program.
Users can register, log in, browse festivals, manage profiles, write reviews, and interact with event content.
The system includes role-based pages and an integrated login for three user types: User, Company, and Admin.

All commits under the local username annabel and GitHub account Bell-alt represent my individual contributions.

🚀 Features

User registration & login (session-based Spring Security authentication)

Role-based UI rendering (User / Company / Admin)

MyPage module — full profile management (update, delete, password change)

Festival listing & detail pages

Review/Comment module

Admin features

Hybrid architecture (MVC + REST)

🛠 Tech Stack
Backend

Java 17

Spring Boot

Spring MVC

Spring Security (session authentication)

MyBatis

JPA (if applicable in parts)

Database

MySQL (with DBeaver)

Oracle SQL Developer

Frontend

HTML5, CSS3, JavaScript

Thymeleaf

JSP (legacy parts)

Tools

Git / GitHub

IntelliJ IDEA

Linux (Ubuntu)

Windows (later development environment)

✨ My Role & Contributions
🔹 Backend Development

Designed & optimized DTO structures

e.g., split MemberDTO into JoinDTO, LoginDTO, and reorganized MemberRole

Implemented REST API endpoints

Added new controller methods (MemberRestController)

Refactored controllers for cleaner separation

MemberController → MemberViewController

Implemented session-based login & validation

Connected service layer to MySQL/Oracle

Fixed & improved Security configuration

🔹 MyPage Module (My primary ownership)

Built the entire MyPage feature end-to-end

CRUD for user profiles

Password update workflow (+ secure hashing)

Phone/email update, nickname changes

Error handling + validations

Developed responsive UI for MyPage (HTML/CSS/JS)

🔹 Database Design

Performed DB normalization and table unification

Unified Company/User tables → shared login system

Executed migrations using:

ALTER TABLE ... MODIFY COLUMN ...


Ensured login compatibility for all user types (User, Company, Admin)

🔹 Additional Contributions

Took part in initial system architecture design

Helped set up Spring Boot project structure

Resolved merge conflicts during team integration

Maintained two branches after moving development from Linux → Windows

📂 Branch Information

MemberBforlater – early user module development

mypage – main branch for MyPage implementation

annabel (local) – Linux-based development

Bell-alt – Windows-based commits after laptop failure

📸 Screenshots / Demo

(You can add GIFs later — I can help you create them.)

▶ How to Run
git clone https://github.com/bbc2281/project_festival
cd project_festival
./gradlew bootRun

Database Setup

Create MySQL schema: festival_db

Update application.properties:

spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

🧾 About Me

I am a backend developer focused on Java, Spring Boot, clean architecture, and building scalable, maintainable systems.
My strengths include authentication design, service architecture, and data modeling.

📬 Contact

GitHub: https://github.com/Bell-alt

Email: uupittee@naver.com

