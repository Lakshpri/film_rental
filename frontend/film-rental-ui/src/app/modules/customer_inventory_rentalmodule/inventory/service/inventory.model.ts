export interface Inventory {
  inventoryId: number;
  filmId: number;
  storeId: number;
  lastUpdate: string;
}

export interface InventoryRequest {
  filmId: number;
  storeId: number;
}
