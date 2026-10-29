interface PatternBase {
  name: string;
  category: PatternCategory;
  shortDescription: string | null;
  description: string | null;
}

export interface PatternRequest extends PatternBase {
  shortDescription: string;
  description: string;
}

export interface PatternResponse extends PatternBase {
  id: number;
  imageUrl: string | null;
}

export type PatternCategory = 'CREATIONAL' | 'STRUCTURAL' | 'BEHAVIORAL';
