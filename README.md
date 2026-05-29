# SkillForge | E-Learning Platform

A full-stack e-learning platform built with Angular, Spring Boot, and PostgreSQL. Features course creation, lesson tracking, quiz auto-grading, and a student analytics dashboard — with role-based access for instructors and students.

## Features
- **Dual role registration**: Instructor and Student roles with Spring Security JWT authentication and BCrypt password hashing.
- **Course CRUD**: Instructors create and manage courses. Students browse and enroll with real-time enrollment state tracking.
- **Lesson management**: Ordered lessons within courses with progress tracking. Students mark lessons complete and track progress per course.
- **Quiz engine**: Multiple-choice quizzes with auto-grading against stored correct answers. Single-attempt enforcement with passing score thresholds and attempt history.
- **Student dashboard**: Enrolled courses with lesson progress bars and quiz completion status. Quick navigation to course content.
- **Instructor controls**: Add lessons, create quizzes, and add questions with options and correct answers — all through the UI.
- **Quiz preview for instructors**: Course owners can view quiz questions with correct answers highlighted in green without attempting the quiz.
- **Attempt tracking**: Students see their attempt history with scores, pass/fail status, and percentage breakdowns.
- **Fully responsive**: Tailwind CSS styling with responsive grids for course cards, lesson lists, and quiz interfaces.

## Technologies Used
- **Angular**: Frontend framework with standalone components
- **TypeScript**: Type-safe development
- **Spring Boot 4.0**: REST API with embedded Tomcat server
- **Hibernate**: ORM for PostgreSQL database mapping
- **Spring Security**: JWT authentication with role-based access control
- **Spring Data JPA**: Repository pattern for database operations
- **PostgreSQL**: Relational database hosted on Supabase
- **Maven**: Build tool and dependency management
- **Tailwind CSS**: Utility-first styling
- **Docker**: Multi-stage build for deployment
- **Render**: Backend deployment
- **Vercel**: Frontend deployment

## Database Schema

### Entity-Relationship Diagram

```
users ────➤ courses ────➤ lessons
│ │ │
│ │ └── lesson_progress (student ↔ lesson)
│ │
│ ├──➤ quizzes ────➤ questions ────➤ question_options
│ │
│ └──➤ enrollments (student ↔ course)
│
└──➤ quiz_attempts (student ↔ quiz)
```


### Tables (9 Total)
| Table | Purpose | Key Relationships |
|-------|---------|-------------------|
| users | Instructors and Students | — |
| courses | Created by instructors | FK to users(instructor_id) |
| lessons | Belongs to courses, ordered | FK to courses, UNIQUE(course_id, order_index) |
| enrollments | Student to Course junction | Composite PK (student_id, course_id) |
| lesson_progress | Student to Lesson tracking | Composite PK (student_id, lesson_id) |
| quizzes | One per course | FK to courses |
| questions | Belongs to quizzes | FK to quizzes, UNIQUE(quiz_id, order_index) |
| question_options | MCQ choices per question | FK to questions, UNIQUE(question_id, option_label) |
| quiz_attempts | Student quiz results | FK to users, FK to quizzes |

### Key Design Decisions
- UUID primary keys on all tables for security and scalability
- Composite keys on junction tables to enforce business rules at the database level
- order_index columns for lessons and questions to support reordering
- correct_answer stored on questions (not options) as single source of truth
- ON DELETE CASCADE for automatic cleanup of related records
- CHECK constraints for data validation at the database level
- Quiz at course level for simplicity with score-only storage for attempts

## Project Structure

### Backend (`backend/`)

```
backend/
├── src/main/java/com/skillforge/backend/
│ ├── config/
│ │ ├── CorsConfig.java # CORS configuration for cross-origin requests
│ │ └── SecurityConfig.java # Spring Security filter chain and public routes
│ ├── controller/
│ │ ├── AuthController.java # Register and login endpoints
│ │ ├── CourseController.java # Course CRUD endpoints
│ │ ├── EnrollmentController.java # Enroll and unenroll endpoints
│ │ ├── LessonController.java # Lesson CRUD endpoints
│ │ ├── LessonProgressController.java # Mark complete and progress tracking
│ │ ├── QuestionController.java # Question and options endpoints
│ │ ├── QuizController.java # Quiz creation and submission
│ │ └── UserController.java # Current user endpoint
│ ├── dto/
│ │ ├── CourseResponse.java # Course DTO with instructor name
│ │ ├── EnrollmentResponse.java # Enrollment DTO
│ │ ├── LessonProgressResponse.java # Progress DTO
│ │ └── LessonResponse.java # Lesson DTO
│ ├── entity/
│ │ ├── User.java # User entity with role enum
│ │ ├── Course.java # Course entity
│ │ ├── Lesson.java # Lesson entity with order_index
│ │ ├── Enrollment.java # Enrollment junction entity
│ │ ├── EnrollmentId.java # Composite key for enrollment
│ │ ├── LessonProgress.java # Lesson progress junction entity
│ │ ├── LessonProgressId.java # Composite key for lesson progress
│ │ ├── Quiz.java # Quiz entity with passing score
│ │ ├── Question.java # Question entity with correct answer
│ │ ├── QuestionOption.java # MCQ option entity
│ │ └── QuizAttempt.java # Quiz attempt entity
│ ├── repository/
│ │ └── *Repository.java # Spring Data JPA repositories (9 total)
│ ├── security/
│ │ ├── JwtUtil.java # JWT generation and validation
│ │ └── JwtAuthenticationFilter.java # Request filter for JWT extraction
│ ├── service/
│ │ ├── AuthService.java # Login logic and token generation
│ │ ├── UserService.java # User registration and lookup
│ │ ├── CourseService.java # Course business logic with ownership checks
│ │ ├── LessonService.java # Lesson business logic
│ │ ├── EnrollmentService.java # Enrollment with duplicate prevention
│ │ ├── LessonProgressService.java # Progress tracking with role checks
│ │ ├── QuizService.java # Quiz grading and attempt management
│ │ └── QuestionService.java # Question and options management
│ └── BackendApplication.java # Spring Boot entry point
├── src/main/resources/
│ └── application.yml # Database, JPA, and JWT configuration
├── Dockerfile # Multi-stage Docker build for Render
└── pom.xml # Maven dependencies and build config
```

### Frontend (`frontend/`)

```
frontend/
├── src/
│ ├── app/
│ │ ├── components/
│ │ │ └── nav/ # Navigation bar component
│ │ ├── pages/
│ │ │ ├── login/ # Login page with form validation
│ │ │ ├── register/ # Registration page with role selection
│ │ │ ├── courses/ # Course listing with create and enroll
│ │ │ ├── course-detail/ # Lessons, progress, and quiz interface
│ │ │ └── dashboard/ # Student dashboard with progress bars
│ │ ├── services/
│ │ │ ├── auth.ts # Auth service with JWT management
│ │ │ └── course.ts # Course service with all API calls
│ │ ├── models/ # TypeScript interfaces
│ │ ├── app.html # Root template with router outlet
│ │ ├── app.ts # Root component
│ │ ├── app-module.ts # Angular module declarations
│ │ └── app-routing-module.ts # Route configuration
│ ├── environments/
│ │ ├── environment.ts # Development API URL
│ │ └── environment.prod.ts # Production API URL
│ ├── styles.css # Tailwind imports
│ └── index.html # Single page entry point
├── angular.json # Angular build configuration
├── package.json # npm dependencies
├── postcss.config.js # PostCSS configuration for Tailwind
└── tsconfig.json # TypeScript configuration
```

### Navigation Structure
The navbar includes links to:
- SkillForge (logo, links to courses)
- Courses (/courses)
- Login (/login)
- Register (/register)
- Dashboard (/dashboard, authenticated only)
- Logout (authenticated only)

### API Endpoints
| Method | Endpoint | Access |
|--------|----------|--------|
| POST | /api/auth/register | Public |
| POST | /api/auth/login | Public |
| GET | /api/courses | Public |
| GET | /api/courses/{id} | Public |
| POST | /api/courses | Instructor |
| PUT | /api/courses/{id} | Owner only |
| DELETE | /api/courses/{id} | Owner only |
| GET | /api/courses/{id}/lessons | Public |
| POST | /api/courses/{id}/lessons | Owner only |
| POST | /api/courses/{id}/enroll | Student |
| DELETE | /api/courses/{id}/enroll | Student |
| GET | /api/enrollments/mine | Authenticated |
| POST | /api/lessons/{id}/complete | Student |
| GET | /api/courses/{id}/progress | Authenticated |
| GET | /api/courses/{id}/quiz | Authenticated |
| POST | /api/courses/{id}/quiz | Owner only |
| POST | /api/quizzes/{id}/submit | Student |
| GET | /api/quizzes/{id}/attempts | Authenticated |
| GET | /api/quizzes/{id}/questions | Authenticated |
| POST | /api/quizzes/{id}/questions | Owner only |
| GET | /api/questions/{id}/options | Authenticated |

### Data Flow
- **Authentication:** Spring Security filter chain intercepts every request. JwtAuthenticationFilter extracts the JWT from the Authorization header, validates it, and sets the security context. Public endpoints bypass authentication.
- **Authorization:** Role-based access at both the controller level (Instructor-only endpoints) and service level (ownership checks for course modifications).
- **Database:** Hibernate ORM maps 9 entities to PostgreSQL tables. Spring Data JPA repositories provide save, find, and delete operations with custom query methods.
- **Frontend State:** AuthService manages JWT in localStorage. CourseService handles all API calls with token-attached headers. ChangeDetectorRef triggers UI updates after async operations.

## Deployment
- **Backend:** Deployed on Render via Docker multi-stage build. Maven compiles the application, then runs on Eclipse Temurin JRE.
- **Frontend:** Deployed on Vercel with automatic builds on push. Angular compiles to static files served via Vercel's CDN.
- **Database:** Supabase PostgreSQL with connection pooling via PgBouncer.

**Live App:** [https://skill-forge-learning.vercel.app](https://skill-forge-learning.vercel.app)

## Usage
SkillForge has the following main sections:
- **Courses:** Browse available courses. Instructors see a Create Course button and Manage Course links for their own courses. Students see Enroll buttons that switch to View Course after enrollment.
- **Course Detail:** View lessons in order with Mark Complete buttons. Take the quiz after studying — one attempt only with immediate scoring.
- **Dashboard:** See all enrolled courses with lesson progress bars and quiz completion status. Quick links to jump back into any course.
- **Instructor Controls:** On your own courses, add lessons with title, content, and ordering. Create quizzes with a passing score threshold. Add multiple-choice questions with four options and a correct answer.

## Contact Information
If you'd like to get in touch with me, here are the best ways to reach me:
- **Email:** [deancruzgg@gmail.com](mailto:deancruzgg@gmail.com)
- **GitHub:** [https://github.com/deancruz1](https://github.com/deancruz1)
- **LinkedIn:** [https://www.linkedin.com/in/dean-cruz/](https://www.linkedin.com/in/dean-cruz/)
- **Location:** Singapore, SG
