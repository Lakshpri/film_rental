export interface CustomerBalance {
  customerId: number;
  totalPayments: number;
  totalAmountPaid: number;
}

export interface RewardsReport {
  totalCustomers: number;
  totalRentals: number;
  averageRentalsPerCustomer: number;
  customers: RewardsCustomer[];
}

export interface RewardsCustomer {
  customerId: number;
  firstName: string;
  lastName: string;
  email: string;
  rentalCount: number;
}

export interface StockResult {
  filmId: number;
  storeId: number;
  inStockCount?: number;
  notInStockCount?: number;
  isAvailable?: boolean;
  message: string;
  inventoryIds: number[];
}
