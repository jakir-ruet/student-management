- Step 1: Authentication Module - Database Design (Tables)
- Step 2: Authentication Module - Packages

### Modules and Development Order

```bash
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
```

### Auth Endpoints

- POST http://localhost:8085/api/auth/register
- POST http://localhost:8085/api/auth/login
- POST http://localhost:8085/api/auth/change-password
- POST http://localhost:8085/api/auth/lock/1
- POST http://localhost:8085/api/auth/unlock/1

### Student Endpoints

- POST http://localhost:8085/api/students
- PUT http://localhost:8085/api/students/1
- DELETE http://localhost:8085/api/students/1
- GET http://localhost:8085/api/students/1
- GET http://localhost:8085/api/students
- GET http://localhost:8085/api/students/search?q=Jakir

### Student Address Endpoints

- POST http://localhost:8085/api/student-addresses
- PUT http://localhost:8085/api/student-addresses/1
- DELETE http://localhost:8085/api/student-addresses/1
- GET http://localhost:8085/api/student-addresses/1
- GET http://localhost:8085/api/student-addresses/student/1

### Student Guardian Endpoints

- POST http://localhost:8085/api/student-guardians
- PUT http://localhost:8085/api/student-guardians/1
- DELETE http://localhost:8085/api/student-guardians/1
- GET http://localhost:8085/api/student-guardians/1
- GET http://localhost:8085/api/student-guardians/student/1

### Teacher Endpoints

- POST http://localhost:8085/api/teachers
- PUT http://localhost:8085/api/teachers/1
- DELETE http://localhost:8085/api/teachers/1
- GET http://localhost:8085/api/teachers/1
- GET http://localhost:8085/api/teachers
- GET http://localhost:8085/api/teachers/search?q=Jakir

### Teacher Address Endpoints

- POST http://localhost:8085/api/teacher-addresses
- PUT http://localhost:8085/api/teacher-addresses/1
- DELETE http://localhost:8085/api/teacher-addresses/1
- GET http://localhost:8085/api/teacher-addresses/teacher/1

### Teacher Contact Endpoints

- POST http://localhost:8085/api/teacher-contacts
- PUT http://localhost:8085/api/teacher-contacts/1
- DELETE http://localhost:8085/api/teacher-contacts/1
- GET http://localhost:8085/api/teacher-contacts/teacher/1

### gitattributes configure

- Create a `.gitattributes`

```bash
* text=auto

# Shell scripts
*.sh text eol=lf

# Windows batch files
*.bat text eol=crlf
*.cmd text eol=crlf

# Optional
*.ps1 text eol=crlf
```

```bash
git config --global core.autocrlf input # Linux/macOS (and WSL)
```

```bash
git config --global core.autocrlf true # Windows 11 (Git for Windows)
```

- Normalize the repository (once) - After adding .gitattributes, run:

```bash
git add --renormalize .
git commit -m "Normalize line endings"
```
