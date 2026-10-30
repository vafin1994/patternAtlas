import { Component, inject } from '@angular/core';
import { PatternApi } from '../../services/pattern-api';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { rxResource } from '@angular/core/rxjs-interop';

@Component({
  imports: [RouterLink],
  selector: 'app-pattern-detail',
  styleUrl: './pattern-detail.less',
  templateUrl: './pattern-detail.html',
})
export class PatternDetail {
  private readonly patternApi: PatternApi = inject(PatternApi);
  private readonly activeRoute: ActivatedRoute = inject(ActivatedRoute);
  private readonly router: Router = inject(Router);

  private readonly id = Number(this.activeRoute.snapshot.params['id']);

  pattern = rxResource({
    stream: () => this.patternApi.getById(this.id),
  });

  public deletePattern(id: number) {
    this.patternApi.delete(id).subscribe(() => {
      this.router.navigateByUrl('/patterns');
    });
  }
}
