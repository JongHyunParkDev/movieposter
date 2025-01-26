export interface Poster {
  id: number;
  name: string;
  fileName: string;
  color: string;
  createdDatetime: Date;
}

export interface PosterDetailFile {
  fileName: string;
  fileType: string;
}
