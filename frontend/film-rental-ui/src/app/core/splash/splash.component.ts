import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService, TeamMember } from '../../auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-splash',
  templateUrl: './splash.component.html',
  imports: [CommonModule]
})
export class SplashComponent {
  constructor(public auth: AuthService, private router: Router) {}

  selectMember(member: TeamMember): void {
    this.router.navigate(['/login', member.id]);
  }
}
