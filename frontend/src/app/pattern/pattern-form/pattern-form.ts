import { Component, DestroyRef, inject, signal } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
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
export class PatternForm {
  private readonly patternApi = inject(PatternApi);
  private readonly router: Router = inject(Router);
  private readonly destroyRef = inject(DestroyRef);

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
      validators: [Validators.required, Validators.maxLength(10000)],
    }),
  });

  selectedFile: File | null = null;
  previewUrl = signal<string | null>(null);

  constructor(private formBuilder: FormBuilder) {
    this.destroyRef.onDestroy(() => this.revokePreview());
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
    this.patternApi
      .create(request)
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

  onFileSelect(event: Event) {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0] ?? null;
    if (!file?.type.startsWith('image/')) {
      return;
    }
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
