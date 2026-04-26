export interface Film {
  filmId: number;
  title: string;
  description: string;
  releaseYear: number;
  languageId: number;
  languageName: string;
  originalLanguageId: number;
  originalLanguageName: string;
  rentalDuration: number;
  rentalRate: number;
  length: number;
  replacementCost: number;
  rating: string;
  specialFeatures: string;
  lastUpdate: string;
}

export interface FilmRequest {
  title: string;
  description?: string;
  releaseYear?: number;
  languageId: number;
  originalLanguageId?: number;
  rentalDuration?: number;
  rentalRate?: number;
  length?: number;
  replacementCost?: number;
  rating?: string;
  specialFeatures?: string;
}
