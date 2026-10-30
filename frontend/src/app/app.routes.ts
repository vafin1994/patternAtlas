import { Routes } from '@angular/router';
import { PatternList } from './pattern/pattern-list/pattern-list';
import { PatternForm } from './pattern/pattern-form/pattern-form';

export const routes: Routes = [
  { path: '', redirectTo: 'patterns', pathMatch: 'full' },
  { path: 'patterns', component: PatternList },
  { path: 'patterns/new', component: PatternForm },
  { path: '**', redirectTo: 'patterns' },
];
