import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService, EntityEndpoints, ApiEndpoint } from '../../auth/auth.service';

@Component({
  standalone: true,
  selector: 'app-endpoint-detail',
  templateUrl: './endpoint-detail.component.html',
  imports: [CommonModule]
})
export class EndpointDetailComponent implements OnInit {
  entity: EntityEndpoints | null = null;
  memberColor = '#8b6fff';

  methodColors: Record<string, string> = {
    GET:    '#22c9a0',
    POST:   '#60a5fa',
    PUT:    '#fbbf24',
    DELETE: '#f87171'
  };
  methodBg: Record<string, string> = {
    GET:    '#0a2b22',
    POST:   '#0e1e2b',
    PUT:    '#2b1e0a',
    DELETE: '#2b0e0e'
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    public auth: AuthService
  ) {}

  ngOnInit(): void {
    if (!this.auth.isLoggedIn) { this.router.navigate(['/']); return; }
    const memberId = this.route.snapshot.paramMap.get('memberId') || '';
    const entityIndex = parseInt(this.route.snapshot.paramMap.get('entityIndex') || '0', 10);
    const member = this.auth.getMemberById(memberId);
    if (!member) { this.router.navigate(['/module-home']); return; }
    this.memberColor = member.color;
    this.entity = member.entities[entityIndex] || null;
    if (!this.entity) this.router.navigate(['/module-home']);
  }

  goToCrud(endpoint: ApiEndpoint): void {
  
    if (endpoint.isSubRoute && this.entity) {
      this.router.navigate([this.entity.route]);
    } else {
      this.router.navigate([endpoint.route]);
    }
  }

  goBack(): void {
    this.router.navigate(['/module-home']);
  }

  getMethodColor(method: string): string { return this.methodColors[method] || '#aaa'; }
  getMethodBg(method: string): string { return this.methodBg[method] || '#1a1a24'; }
}
