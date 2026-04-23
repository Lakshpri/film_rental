export interface Address {
  addressId: number;
  address: string;
  address2: string;
  district: string;
  postalCode: string;
  phone: string;
  cityId: number;
  lastUpdate: string;
}

export interface AddressRequest {
  address: string;
  address2?: string;
  district: string;
  postalCode?: string;
  phone: string;
  cityId: number;
}
