import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CourseService } from '../../services/courses';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.html',
  standalone: false,
})
export class Dashboard implements OnInit {
  enrollments: any[] = [];
  loading = true;

  constructor(
    private courseService: CourseService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
  ) {}

  ngOnInit() {
    this.loadDashboard();
  }

  loadDashboard() {
    this.courseService.getMyEnrollments().subscribe({
      next: (enrollments) => {
        this.enrollments = enrollments;
        if (this.enrollments.length === 0) {
          this.loading = false;
          this.cdr.detectChanges();
          return;
        }
        this.loadDetailsForAll();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  loadDetailsForAll() {
    let loaded = 0;
    this.enrollments.forEach((enrollment) => {
      // Get lessons
      this.courseService.getLessons(enrollment.courseId).subscribe({
        next: (lessons) => {
          enrollment.totalLessons = lessons.length;
          this.checkAllLoaded(++loaded);
        },
      });

      // Get progress
      this.courseService.getProgress(enrollment.courseId).subscribe({
        next: (progress) => {
          enrollment.completedLessons = progress.filter((p: any) => p.completed).length;
        },
      });

      // Get quiz
      this.courseService.getQuiz(enrollment.courseId).subscribe({
        next: (quiz) => {
          enrollment.hasQuiz = true;
          this.courseService.getAttempts(quiz.id).subscribe({
            next: (attempts) => {
              enrollment.quizAttempts = attempts.length;
              enrollment.quizPassed = attempts.some((a: any) => a.passed);
              enrollment.quizScore = attempts.length > 0 ? attempts[0].score : null;
              enrollment.quizTotal = attempts.length > 0 ? attempts[0].totalPoints : null;
            },
          });
        },
        error: () => {
          enrollment.hasQuiz = false;
        },
      });
    });
  }

  checkAllLoaded(count: number) {
    if (count >= this.enrollments.length) {
      setTimeout(() => {
        this.loading = false;
        this.cdr.detectChanges();
      }, 500);
    }
  }

  getProgressPercent(enrollment: any): number {
    if (!enrollment.totalLessons || enrollment.totalLessons === 0) return 0;
    return Math.round((enrollment.completedLessons / enrollment.totalLessons) * 100);
  }
}
