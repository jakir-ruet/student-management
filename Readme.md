- Step 1: Authentication Module - Database Design (Tables)
- Step 2: Authentication Module - Packages

### Modules and Development Order

```bash
- Authentication
- Role Management
- Permission Management
- Student Management
- Teacher Management
- ACSS Management # A - Academic, C - Classes, S - Sections and S - Shift
- Subject Management
- Attendance Management
- Examination Management
- Fee Management
- Reporting
- Dashboard
- Audit Logs
```

```bash
✔ Authentication
✔ RBAC (Role & Permission)
✔ Student
✔ Teacher
✔ ACSS # A - Academic, C - Classes, S - Sections and S - Shift
🔄 Subject
⬜ Attendance
⬜ Examination
⬜ Fee
⬜ Reporting
⬜ Dashboard
⬜ Audit Logs
⬜ Spring Security + JWT
⬜ Docker
⬜ CI/CD
⬜ AWS Deployment
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

### Academic Year Endpoints

- POST   http://localhost:8085/api/acss/academic-years
- PUT    http://localhost:8085/api/acss/academic-years/{academicYearId}
- GET    http://localhost:8085/api/acss/academic-years/{academicYearId}
- GET    http://localhost:8085/api/acss/academic-years
- DELETE http://localhost:8085/api/acss/academic-years/{academicYearId}

### Class Endpoints

- POST   http://localhost:8085/api/acss/classes
- PUT    http://localhost:8085/api/acss/classes/{classId}
- GET    http://localhost:8085/api/acss/classes/{classId}
- GET    http://localhost:8085/api/acss/classes
- DELETE http://localhost:8085/api/acss/classes/{classId}

### Academic Year–Class Endpoints - These endpoints open/assign classes under an academic year-

- POST   http://localhost:8085/api/acss/academic-year-classes
- PUT    http://localhost:8085/api/acss/academic-year-classes/{academicYearClassId}
- GET    http://localhost:8085/api/acss/academic-year-classes/{academicYearClassId}
- GET    http://localhost:8085/api/acss/academic-year-classes
- GET    http://localhost:8085/api/acss/academic-year-classes/by-academic-year/{academicYearId}
- DELETE http://localhost:8085/api/acss/academic-year-classes/{academicYearClassId}

### Section Endpoints

- POST   http://localhost:8085/api/acss/sections
- PUT    http://localhost:8085/api/acss/sections/{sectionId}
- GET    http://localhost:8085/api/acss/sections/{sectionId}
- GET    http://localhost:8085/api/acss/sections
- GET    http://localhost:8085/api/acss/sections/by-academic-year-class/{academicYearClassId}
- DELETE http://localhost:8085/api/acss/sections/{sectionId}

### Shift Endpoints

- POST   http://localhost:8085/api/acss/shifts
- PUT    http://localhost:8085/api/acss/shifts/{shiftId}
- GET    http://localhost:8085/api/acss/shifts/{shiftId}
- GET    http://localhost:8085/api/acss/shifts
- DELETE http://localhost:8085/api/acss/shifts/{shiftId}

### Section–Shift Endpoints - These endpoints assign shifts to sections

- POST   http://localhost:8085/api/acss/section-shifts
- PUT    http://localhost:8085/api/acss/section-shifts/{sectionShiftId}
- GET    http://localhost:8085/api/acss/section-shifts/{sectionShiftId}
- GET    http://localhost:8085/api/acss/section-shifts
- GET    http://localhost:8085/api/acss/section-shifts/by-section/{sectionId}
- DELETE http://localhost:8085/api/acss/section-shifts/{sectionShiftId}

### Subject Management endpoints

**Recommended Testing Order**
- Create Subject
- Create Class Subject
- Create Teacher Subject Assignment
- Get by ID
- Get All
- Update
- Delete

- POST   http://localhost:8085/api/subjects
- PUT    http://localhost:8085/api/subjects/{subjectId}
- GET    http://localhost:8085/api/subjects/{subjectId}
- GET    http://localhost:8085/api/subjects
- DELETE http://localhost:8085/api/subjects/{subjectId}

- POST   http://localhost:8085/api/class-subjects
- PUT    http://localhost:8085/api/class-subjects/{classSubjectId}
- GET    http://localhost:8085/api/class-subjects/{classSubjectId}
- GET    http://localhost:8085/api/class-subjects
- GET    http://localhost:8085/api/class-subjects/academic-year-class/{academicYearClassId}
- DELETE http://localhost:8085/api/class-subjects/{classSubjectId}

- POST   http://localhost:8085/api/teacher-subject-assignments
- PUT    http://localhost:8085/api/teacher-subject-assignments/{assignmentId}
- GET    http://localhost:8085/api/teacher-subject-assignments/{assignmentId}
- GET    http://localhost:8085/api/teacher-subject-assignments
- GET    http://localhost:8085/api/teacher-subject-assignments/teacher/{teacherId}
- DELETE http://localhost:8085/api/teacher-subject-assignments/{assignmentId}

### Attendance endpoints

#### Student Enrollment

- POST   http://localhost:8085/api/student-enrollments
- PUT    http://localhost:8085/api/student-enrollments/{enrollmentId}
- GET    http://localhost:8085/api/student-enrollments/{enrollmentId}
- GET    http://localhost:8085/api/student-enrollments
- GET    http://localhost:8085/api/student-enrollments/student/{studentId}
- GET    http://localhost:8085/api/student-enrollments/section-shift/{sectionShiftId}?status=ENROLLED
- DELETE http://localhost:8085/api/student-enrollments/{enrollmentId}

#### Attendance Session

- POST   http://localhost:8085/api/attendance-sessions
- PUT    http://localhost:8085/api/attendance-sessions/{attendanceSessionId}
- GET    http://localhost:8085/api/attendance-sessions/{attendanceSessionId}
- GET    http://localhost:8085/api/attendance-sessions
- GET    http://localhost:8085/api/attendance-sessions/section-shift/{sectionShiftId}?attendanceDate=2026-07-13
- PATCH  http://localhost:8085/api/attendance-sessions/{attendanceSessionId}/status
- DELETE http://localhost:8085/api/attendance-sessions/{attendanceSessionId}

#### Attendance Record

- POST   http://localhost:8085/api/attendance-records
- PUT    http://localhost:8085/api/attendance-records/{attendanceRecordId}
- GET    http://localhost:8085/api/attendance-records/{attendanceRecordId}
- GET    http://localhost:8085/api/attendance-records/session/{attendanceSessionId}
- GET    http://localhost:8085/api/attendance-records/enrollment/{enrollmentId}
- DELETE http://localhost:8085/api/attendance-records/{attendanceRecordId}

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
