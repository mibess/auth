import { Component, Input } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-button-back',
  standalone: true,
  imports: [ RouterLink ],
  templateUrl: './button-back.component.html',
  styleUrl: './button-back.component.scss'
})
export class ButtonBackComponent {
  @Input() url = '/';
  @Input() text = 'Back';
}
