- Step 1: Authentication Module - Database Design (Tables)
- Step 2: Authentication Module - Packages

### Modules and Development Order

- Authentication
- Role Management
- Permission Management
- Student Management
- Teacher Management
- Class & Section Management
- Subject Management
- Attendance Management
- Examination Management
- Fee Management
- Reporting
- Dashboard
- Audit Logs


Endpoints of Auth

POST http://localhost:8080/api/auth/register
POST http://localhost:8080/api/auth/login
POST http://localhost:8080/api/auth/change-password
POST http://localhost:8080/api/auth/lock/1
POST http://localhost:8080/api/auth/unlock/1

common
└── exception
    ├── ApiError.java
    ├── GlobalExceptionHandler.java
    ├── DatabaseException.java
    ├── ResourceNotFoundException.java
    ├── DuplicateResourceException.java
    ├── InvalidCredentialsException.java
    ├── AccountLockedException.java
    ├── InactiveAccountException.java
    ├── BusinessException.java
    └── OracleExceptionTranslator.java