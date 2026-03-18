import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { PrimengModule } from '../../shared/primeng.module';

@Component({
  selector: 'app-reception',
  standalone: true,
  imports: [FormsModule, CommonModule, PrimengModule],
  templateUrl: './reception.html',
  styleUrls: ['./reception.css'],

})
export class Reception {

}
