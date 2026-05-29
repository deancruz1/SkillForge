import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CourseService } from '../../services/courses';
import { AuthService } from '../../services/auth';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-course-detail',
  templateUrl: './course-detail.html',
  standalone: false,
})
export class CourseDetail implements OnInit {
  course: any = null;
  lessons: any[] = [];
  progress: any[] = [];
  loading = true;

  quiz: any = null;
  questions: any[] = [];
  selectedAnswers: Map<string, string> = new Map();
  quizSubmitted = false;
  quizResult: any = null;
  showQuiz = false;
  alreadyAttempted = false;
  attempts: any[] = [];

  showAddLesson = false;
  newLessonTitle = '';
  newLessonContent = '';
  newLessonOrder = 0;

  showCreateQuiz = false;
  newQuizTitle = '';
  newQuizPassingScore = 50;

  showAddQuestion = false;
  newQuestionText = '';
  newOptionA = '';
  newOptionB = '';
  newOptionC = '';
  newOptionD = '';
  newCorrectAnswer = 'A';
  newQuestionOrder = 1;

  constructor(
    private route: ActivatedRoute,
    private courseService: CourseService,
    private authService: AuthService,
    private cdr: ChangeDetectorRef,
    private http: HttpClient,
  ) {}

  ngOnInit() {
    const courseId = this.route.snapshot.paramMap.get('id')!;
    this.loadCourse(courseId);
  }

  loadCourse(courseId: string) {
    this.courseService.getCourse(courseId).subscribe({
      next: (data) => {
        this.course = data;
        this.loadLessons(courseId);
        this.loadQuiz(courseId);
      },
      error: () => (this.loading = false),
    });
  }

  loadLessons(courseId: string) {
    this.courseService.getLessons(courseId).subscribe({
      next: (data) => {
        this.lessons = data;
        this.loadProgress(courseId);
      },
      error: () => (this.loading = false),
    });
  }

  loadProgress(courseId: string) {
    if (!this.authService.isLoggedIn()) {
      this.loading = false;
      this.cdr.detectChanges();
      return;
    }

    this.courseService.getProgress(courseId).subscribe({
      next: (data) => {
        this.progress = data;
        this.loading = false;
        this.cdr.detectChanges();
      },
      error: () => {
        this.loading = false;
        this.cdr.detectChanges();
      },
    });
  }

  isLessonCompleted(lessonId: string): boolean {
    return this.progress.some((p: any) => p.lessonId === lessonId && p.completed);
  }

  markComplete(lessonId: string) {
    this.courseService.completeLesson(lessonId).subscribe({
      next: () => {
        const prog = this.progress.find((p: any) => p.lessonId === lessonId);
        if (prog) {
          prog.completed = true;
        } else {
          this.progress.push({ lessonId, completed: true });
        }
        this.cdr.detectChanges();
      },
    });
  }

  loadQuiz(courseId: string) {
    this.courseService.getQuiz(courseId).subscribe({
      next: (data) => {
        this.quiz = data;
        this.loadQuestions(data.id);
        this.checkPreviousAttempts(data.id);
      },
      error: () => {},
    });
  }

  checkPreviousAttempts(quizId: string) {
    this.courseService.getAttempts(quizId).subscribe({
      next: (attempts: any[]) => {
        this.attempts = attempts;
        if (attempts.length > 0) {
          this.alreadyAttempted = true;
          this.quizSubmitted = true;
          this.quizResult = {
            score: attempts[0].score,
            totalPoints: attempts[0].totalPoints,
            passed: attempts[0].passed,
          };
          this.cdr.detectChanges();
        }
      },
    });
  }

  loadQuestions(quizId: string) {
    this.courseService.getQuestions(quizId).subscribe({
      next: async (questions) => {
        this.questions = questions;
        for (const q of questions) {
          const options = await this.courseService.getOptions(q.id).toPromise();
          q.options = options;
        }
        this.cdr.detectChanges();
      },
    });
  }

  selectAnswer(questionId: string, answer: string) {
    this.selectedAnswers.set(questionId, answer);
  }

  submitQuiz() {
    if (!this.quiz) return;
    const answers = this.questions.map((q) => this.selectedAnswers.get(q.id) || '');
    this.courseService.submitQuiz(this.quiz.id, answers).subscribe({
      next: (result) => {
        this.quizResult = result;
        this.quizSubmitted = true;
        this.alreadyAttempted = true;
        this.cdr.detectChanges();
      },
    });
  }

  toggleQuiz(courseId: string) {
    this.showQuiz = !this.showQuiz;
    if (this.showQuiz && !this.quiz) {
      this.loadQuiz(courseId);
    }
  }

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  get isOwner(): boolean {
    return this.course && this.authService.getUserId() === this.course.instructorId;
  }

  addLesson() {
    if (!this.newLessonTitle.trim()) return;
    this.courseService.getLessons(this.course.id).subscribe({
      next: (lessons) => {
        const orderIndex = this.newLessonOrder || lessons.length + 1;
        this.http
          .post(
            `http://localhost:8080/api/courses/${this.course.id}/lessons`,
            {
              title: this.newLessonTitle,
              content: this.newLessonContent,
              orderIndex: orderIndex,
            },
            { headers: this.getAuthHeaders() },
          )
          .subscribe({
            next: () => {
              this.newLessonTitle = '';
              this.newLessonContent = '';
              this.newLessonOrder = 0;
              this.showAddLesson = false;
              this.loadLessons(this.course.id);
              this.cdr.detectChanges();
            },
          });
      },
    });
  }

  createQuiz() {
    if (!this.newQuizTitle.trim()) return;
    this.http
      .post(
        `http://localhost:8080/api/courses/${this.course.id}/quiz`,
        {
          title: this.newQuizTitle,
          description: '',
          passingScore: this.newQuizPassingScore,
        },
        { headers: this.getAuthHeaders() },
      )
      .subscribe({
        next: () => {
          this.newQuizTitle = '';
          this.showCreateQuiz = false;
          this.loadQuiz(this.course.id);
          this.cdr.detectChanges();
        },
      });
  }

  addQuestion() {
    if (!this.newQuestionText.trim()) return;

    const options = [
      { label: 'A', text: this.newOptionA },
      { label: 'B', text: this.newOptionB },
      { label: 'C', text: this.newOptionC },
      { label: 'D', text: this.newOptionD },
    ];

    const body = {
      questionText: this.newQuestionText,
      correctAnswer: this.newCorrectAnswer,
      orderIndex: this.newQuestionOrder,
      points: 10,
      options: options,
    };

    this.http
      .post(`http://localhost:8080/api/quizzes/${this.quiz.id}/questions`, body, {
        headers: this.getAuthHeaders(),
      })
      .subscribe({
        next: () => {
          this.newQuestionText = '';
          this.newOptionA = '';
          this.newOptionB = '';
          this.newOptionC = '';
          this.newOptionD = '';
          this.newQuestionOrder++;
          this.showAddQuestion = false;
          this.loadQuestions(this.quiz.id);
          this.cdr.detectChanges();
        },
        error: (err) => {
          console.error('Failed to add question:', err);
          alert('Failed to add question');
        },
      });
  }

  getAuthHeaders() {
    const token = this.authService.getToken();
    return { Authorization: `Bearer ${token}` };
  }
}
