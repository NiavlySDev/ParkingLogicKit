import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Reception } from './components/reception/reception'; 


@Component({
  selector: 'app-root',
  imports: [RouterOutlet, Reception],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('ParkingLogicKit');
}
