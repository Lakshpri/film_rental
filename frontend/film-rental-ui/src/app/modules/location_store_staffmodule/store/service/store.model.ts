export interface Store {
  storeId: number;
  managerStaffId: number;
  managerFullName: string;
  addressId: number;
  addressLine: string;
  city: string;
  lastUpdate: string;
}

export interface StoreRequest {
  managerStaffId: number;
  addressId: number;
}
