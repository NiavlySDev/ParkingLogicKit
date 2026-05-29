import { Component, OnInit } from '@angular/core';
import { RouterLink } from '@angular/router';
import { PrimengModule } from '../../shared/primeng.module';
import { UpdateCheckService } from '../../services/update-check.service';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [PrimengModule, RouterLink],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {
  appVersion = '';

  constructor(private updateCheckService: UpdateCheckService) {}

  async ngOnInit(): Promise<void> {
    this.appVersion = await this.updateCheckService.getCurrentVersion();
  }
}
