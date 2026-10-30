import { Routes } from '@angular/router';
import { PatternList } from './pattern/pattern-list/pattern-list';
import { PatternForm } from './pattern/pattern-form/pattern-form';
import { PatternDetail } from './pattern/pattern-detail/pattern-detail';

export const routes: Routes = [
  { path: '', redirectTo: 'patterns', pathMatch: 'full' },
  { path: 'patterns', component: PatternList },
  { path: 'patterns/new', component: PatternForm },
  { path: 'patterns/:id', component: PatternDetail },
  { path: 'patterns/:id/edit', component: PatternForm },
  { path: '**', redirectTo: 'patterns' },
];
