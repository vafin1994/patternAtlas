import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { PatternList } from './pattern/pattern-list/pattern-list';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, PatternList],
  templateUrl: './app.html',
  styleUrl: './app.less',
})
export class App {
  protected readonly title = signal('frontend');
}
