export interface City {
  cityId: number;
  city: string;
  countryId: number;
  lastUpdate: string;
}

export interface CityRequest {
  city: string;
  countryId: number;
}
