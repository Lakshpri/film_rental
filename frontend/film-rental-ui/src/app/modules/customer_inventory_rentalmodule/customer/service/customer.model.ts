export interface Customer {
  customerId: number;
  firstName: string;
  lastName: string;
  email: string;
  active: boolean;
  storeId: number;
  addressId: number;
  addressLine: string;
  createDate: string;
  lastUpdate: string;
}

export interface CustomerRequest {
  firstName: string;
  lastName: string;
  email?: string;
  active: boolean;
  storeId: number;
  addressId: number;
}
