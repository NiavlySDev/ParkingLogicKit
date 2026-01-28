import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [PrimengModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home {}
