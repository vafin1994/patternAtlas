import { inject, Service } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { PatternCategory, PatternRequest, PatternResponse } from '../interfaces/Pattern.interface';
import { Observable } from 'rxjs';

@Service()
export class PatternApi {
  private readonly http: HttpClient = inject(HttpClient);
  private readonly baseUrl: string = `/api/patterns`;

  public getAll(category?: PatternCategory): Observable<PatternResponse[]> {
    let params: HttpParams = new HttpParams();
    if (category) {
      params = params.set('category', category);
    }
    return this.http.get<PatternResponse[]>(this.baseUrl, { params });
  }

  public getById(id: number): Observable<PatternResponse> {
    return this.http.get<PatternResponse>(this.baseUrl + `/${id}`);
  }

  public postImage(id: number, image: File): Observable<PatternResponse> {
    const imageFormData: FormData = new FormData();
    imageFormData.append('file', image);
    return this.http.post<PatternResponse>(this.baseUrl + `/${id}` + '/image', imageFormData);
  }

  public create(pattern: PatternRequest): Observable<PatternResponse> {
    return this.http.post<PatternResponse>(this.baseUrl, pattern);
  }

  public update(id: number, pattern: PatternRequest): Observable<PatternResponse> {
    return this.http.put<PatternResponse>(this.baseUrl + `/${id}`, pattern);
  }

  public delete(id: number): Observable<void> {
    return this.http.delete<void>(this.baseUrl + `/${id}`);
  }
}
