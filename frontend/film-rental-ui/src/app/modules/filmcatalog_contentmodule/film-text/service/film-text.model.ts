export interface FilmText {
  filmId: number;
  title: string;
  description: string;
}

export interface FilmTextRequest {
  filmId: number;
  title: string;
  description?: string;
}
