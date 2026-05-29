import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Login } from './pages/login/login';
import { Register } from './pages/register/register';
import { Courses } from './pages/courses/courses';
import { Dashboard } from './pages/dashboard/dashboard';
import { CourseDetail } from './pages/course-detail/course-detail';

const routes: Routes = [
  { path: 'login', component: Login },
  { path: 'register', component: Register },
  { path: 'courses', component: Courses },
  { path: 'courses/:id', component: CourseDetail },
  { path: 'dashboard', component: Dashboard },
  { path: '**', redirectTo: 'courses' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule],
})
export class AppRoutingModule {}
