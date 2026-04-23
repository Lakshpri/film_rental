export interface Rental {
  rentalId: number;
  rentalDate: string;
  returnDate: string;
  inventoryId: number;
  customerId: number;
  staffId: number;
  lastUpdate: string;
}

export interface RentalRequest {
  rentalDate: string;
  inventoryId: number;
  customerId: number;
  staffId: number;
}
