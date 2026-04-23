import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService, TeamMember } from '../../auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-login',
  templateUrl: './login.component.html',
  imports: [CommonModule, FormsModule]
})
export class LoginComponent implements OnInit {
  member: TeamMember | null = null;
  password = '';
  error = '';
  loading = false;
  showPassword = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public auth: AuthService
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id') || '';
    this.member = this.auth.getMemberById(id) || null;
    if (!this.member) this.router.navigate(['/']);
  }

  login(): void {
    if (!this.member || !this.password) {
      this.error = 'Please enter your password.';
      return;
    }
    this.loading = true;
    this.error = '';

    this.auth.login(this.member.username, this.password).subscribe(success => {
      this.loading = false;
      if (success) {
        this.router.navigate(['/module-home']);
      } else {
        this.error = 'Incorrect password. Hint: try 1234';
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/']);
  }

  onKeydown(event: KeyboardEvent): void {
    if (event.key === 'Enter') this.login();
  }
}
