import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import {
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { PatternApi } from '../../services/pattern-api';
import {
  PatternCategory,
  PatternRequest,
  PatternResponse,
} from '../../interfaces/Pattern.interface';
import { of, switchMap, take } from 'rxjs';

@Component({
  imports: [ReactiveFormsModule, RouterLink],
  selector: 'app-pattern-form',
  styleUrl: './pattern-form.less',
  templateUrl: './pattern-form.html',
})
export class PatternForm implements OnInit {
  private readonly patternApi = inject(PatternApi);
  private readonly router: Router = inject(Router);
  private readonly activeRoute: ActivatedRoute = inject(ActivatedRoute);
  private readonly destroyRef = inject(DestroyRef);

  public mode: 'new' | 'edit' = 'new';

  private readonly rawId = this.activeRoute.snapshot.params['id'];
  public readonly patternId: number | null = this.rawId ? Number(this.rawId) : null;

  patternForm = new FormGroup({
    name: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.minLength(3), Validators.maxLength(255)],
    }),
    category: new FormControl<PatternCategory | null>(null, Validators.required),
    shortDescription: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(255)],
    }),
    description: new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.maxLength(10000)],
    }),
  });

  selectedFile: File | null = null;
  previewUrl = signal<string | null>(null);

  constructor() {
    this.destroyRef.onDestroy(() => this.revokePreview());
  }

  ngOnInit() {
    if (this.patternId) {
      this.mode = 'edit';
      this.patternApi.getById(this.patternId).subscribe((res: PatternResponse) => {
        this.patternForm.setValue({
          name: res.name,
          category: res.category,
          shortDescription: res.shortDescription ?? '',
          description: res.description ?? '',
        });
        this.previewUrl.set(res.imageUrl);
      });
    } else {
      this.mode = 'new';
    }
  }

  onFormSubmit() {
    if (this.patternForm.invalid) {
      this.patternForm.markAllAsTouched();
      return;
    }
    const form = this.patternForm.getRawValue();
    const request: PatternRequest = {
      name: form.name,
      category: form.category!,
      shortDescription: form.shortDescription,
      description: form.description,
    };
    if (this.mode === 'edit') {
      this.updatePattern(request);
    } else {
      this.createPattern(request);
    }
  }

  private createPattern(pattern: PatternRequest) {
    this.patternApi
      .create(pattern)
      .pipe(
        take(1),
        switchMap((res: PatternResponse) => {
          if (this.selectedFile) {
            return this.patternApi.postImage(res.id, this.selectedFile);
          }
          return of(res);
        }),
      )
      .subscribe((res: PatternResponse) => {
        this.router.navigateByUrl('/patterns');
      });
  }

  private updatePattern(pattern: PatternRequest) {
    this.patternApi
      .update(this.patternId!, pattern)
      .pipe(
        take(1),
        switchMap((res: PatternResponse) => {
          if (this.selectedFile) {
            return this.patternApi.postImage(res.id, this.selectedFile);
          }
          return of(res);
        }),
      )
      .subscribe((res: PatternResponse) => {
        this.router.navigate(['/patterns', res.id]);
      });
  }

  onFileSelect(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    if (!file?.type.startsWith('image/')) {
      return;
    }
    this.revokePreview();
    this.previewUrl.set(URL.createObjectURL(file));
    this.selectedFile = file;
    input.value = '';
  }

  onFileRemove() {
    this.selectedFile = null;
    this.revokePreview();
  }

  private revokePreview() {
    const url = this.previewUrl();
    if (url) URL.revokeObjectURL(url);
    this.previewUrl.set(null);
  }
}
