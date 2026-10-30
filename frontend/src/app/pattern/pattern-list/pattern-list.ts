import { Component, inject, signal, WritableSignal } from '@angular/core';
import { PatternApi } from '../../services/pattern-api';
import { PatternCategory } from '../../interfaces/Pattern.interface';
import { rxResource } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';

@Component({
  imports: [RouterLink],
  selector: 'app-pattern-list',
  styleUrl: './pattern-list.less',
  templateUrl: './pattern-list.html',
})
export class PatternList {
  private readonly patternApi: PatternApi = inject(PatternApi);

  public readonly selectedCategory: WritableSignal<PatternCategory | undefined> = signal<
    PatternCategory | undefined
  >(undefined);

  public readonly patternsList = rxResource({
    params: () => ({ category: this.selectedCategory() }),
    stream: ({ params }) => this.patternApi.getAll(params.category),
  });

  public selectCategory(category?: PatternCategory) {
    this.selectedCategory.set(category);
  }
}
