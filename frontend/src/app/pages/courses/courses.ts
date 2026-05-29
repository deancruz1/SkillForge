import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CourseService } from '../../services/courses';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.html',
  standalone: false,
})
export class Courses implements OnInit {
  courses: any[] = [];
  enrolledCourseIds: Set<string> = new Set();
  loading = true;

  constructor(
    private courseService: CourseService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.loadCourses();
  }

  loadCourses() {
    this.loading = true;
    this.courseService.getAllCourses().subscribe({
      next: (data) => {
        this.courses = data;
        this.loadEnrollments();
      },
      error: (err) => {
        console.error('Failed to load courses:', err);
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  loadEnrollments() {
    if (!this.authService.isLoggedIn()) {
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }

    this.courseService.getMyEnrollments().subscribe({
      next: (enrollments) => {
        this.enrolledCourseIds = new Set(enrollments.map((e: any) => e.courseId));
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  enroll(courseId: string) {
    this.courseService.enroll(courseId).subscribe({
      next: () => {
        this.enrolledCourseIds.add(courseId);
        this.cdr.detectChanges();
      },
      error: () => alert('Enrollment failed.'),
    });
  }

  isEnrolled(courseId: string): boolean {
    return this.enrolledCourseIds.has(courseId);
  }

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  get isInstructor(): boolean {
    return this.authService.isInstructor();
  }

  showCreateForm = false;
  newCourseTitle = '';
  newCourseDescription = '';

  createCourse() {
    if (!this.newCourseTitle.trim()) return;
    this.courseService.createCourse(this.newCourseTitle, this.newCourseDescription).subscribe({
      next: () => {
        this.newCourseTitle = '';
        this.newCourseDescription = '';
        this.showCreateForm = false;
        this.loadCourses();
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to create course:', err);
        alert('Failed to create course');
      },
    });
  }

  isOwner(instructorId: string): boolean {
    return this.authService.getUserId() === instructorId;
  }
}
