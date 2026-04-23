import { Component } from '@angular/core';
import { Router, NavigationEnd, RouterOutlet, RouterLink, RouterLinkActive } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from './auth/auth.service';
@Component({
  standalone: true,
  selector: 'app-root',
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  isPublicRoute = true;

  constructor(public auth: AuthService, private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        const url = event.urlAfterRedirects;
        this.isPublicRoute = url === '/'
          || url.startsWith('/login')
          || url === '/module-home'
          || url === '/dashboard'
          || url.startsWith('/endpoint-detail');
      }
    });
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}


