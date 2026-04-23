export interface Payment {
  paymentId: number;
  customerId: number;
  staffId: number;
  rentalId: number;
  amount: number;
  paymentDate: string;
}

export interface PaymentRequest {
  customerId: number;
  staffId: number;
  rentalId?: number;
  amount: number;
  paymentDate: string;
}
