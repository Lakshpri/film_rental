import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService, EntityEndpoints } from '../../auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-module-home',
  templateUrl: './module-home.component.html',
  imports: [CommonModule]
})
export class ModuleHomeComponent implements OnInit {

  constructor(public auth: AuthService, private router: Router) {}

  ngOnInit(): void {
    if (!this.auth.isLoggedIn) this.router.navigate(['/']);
  }

  goToEntity(entity: EntityEndpoints): void {
    const member = this.auth.currentMember!;
    const entityIndex = member.entities.indexOf(entity);
    this.router.navigate(['/endpoint-detail', member.id, entityIndex]);
  }

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/']);
  }
}
