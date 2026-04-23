import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from './auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(AuthService);
  if (auth.credentials) {
    const authReq = req.clone({
      setHeaders: { Authorization: `Basic ${auth.credentials}` }
    });
    return next(authReq);
  }
  return next(req);
};
