import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth';

@Injectable({
  providedIn: 'root',
})
export class CourseService {
  private apiUrl = 'http://localhost:8080/api/courses';

  constructor(
    private http: HttpClient,
    private authService: AuthService,
  ) {}

  private getHeaders(): HttpHeaders {
    const token = this.authService.getToken();
    return new HttpHeaders({
      Authorization: `Bearer ${token}`,
    });
  }

  getAllCourses(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  getCourse(id: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }

  createCourse(title: string, description: string): Observable<any> {
    return this.http.post(this.apiUrl, { title, description }, { headers: this.getHeaders() });
  }

  enroll(courseId: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/${courseId}/enroll`, {}, { headers: this.getHeaders() });
  }

  getMyEnrollments(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/enrollments/mine', {
      headers: this.getHeaders(),
    });
  }

  getLessons(courseId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/${courseId}/lessons`, {
      headers: this.getHeaders(),
    });
  }

  getProgress(courseId: string): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/api/courses/${courseId}/progress`, {
      headers: this.getHeaders(),
    });
  }

  completeLesson(lessonId: string): Observable<any> {
    return this.http.post(
      `http://localhost:8080/api/lessons/${lessonId}/complete`,
      {},
      { headers: this.getHeaders() },
    );
  }

  getQuiz(courseId: string): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/api/courses/${courseId}/quiz`, {
      headers: this.getHeaders(),
    });
  }

  getQuestions(quizId: string): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/api/quizzes/${quizId}/questions`, {
      headers: this.getHeaders(),
    });
  }

  getOptions(questionId: string): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/api/questions/${questionId}/options`, {
      headers: this.getHeaders(),
    });
  }

  submitQuiz(quizId: string, answers: string[]): Observable<any> {
    return this.http.post(
      `http://localhost:8080/api/quizzes/${quizId}/submit`,
      { answers },
      { headers: this.getHeaders() },
    );
  }

  getAttempts(quizId: string): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/api/quizzes/${quizId}/attempts`, {
      headers: this.getHeaders(),
    });
  }
}
