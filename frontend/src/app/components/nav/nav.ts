import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth';

@Component({
  selector: 'app-nav',
  templateUrl: './nav.html',
  standalone: false,
})
export class Nav {
  title = 'SkillForge';

  constructor(
    private authService: AuthService,
    private router: Router,
  ) {}

  get isLoggedIn(): boolean {
    return this.authService.isLoggedIn();
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
