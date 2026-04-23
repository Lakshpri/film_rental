export interface Staff {
  staffId: number;
  firstName: string;
  lastName: string;
  email: string;
  username: string;
  active: boolean;
  storeId: number;
  addressId: number;
  lastUpdate: string;
}

export interface StaffRequest {
  firstName: string;
  lastName: string;
  email?: string;
  username: string;
  password?: string;
  active: boolean;
  addressId: number;
  storeId: number;
}
