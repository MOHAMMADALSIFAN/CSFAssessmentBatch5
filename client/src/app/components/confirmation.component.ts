import { Component, inject, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  standalone: false,
  templateUrl: './confirmation.component.html',
  styleUrl: './confirmation.component.css',
})
export class ConfirmationComponent implements OnInit {
  // TODO: Task 5
  orderId: string = '';
  paymentId: string = '';
  total: number = 0;
  timestamp: number = 0;
  formattedDate: string = '';

  private router = inject(Router);

  ngOnInit(): void {
    const state = history.state;

    console.log('State in confirmation component:', state);

    if (state && state.orderData) {
      this.orderId = state.orderData.order_id;
      this.paymentId = state.orderData.payment_id;
      this.total = state.orderData.total;
      this.timestamp = state.orderData.timestamp;
      this.formattedDate = new Date(this.timestamp).toLocaleString();
    } else {
      console.error('No order data found in state');
      this.router.navigate(['/menu']);
    }
  }
  goBackToMenu(): void {
    this.router.navigate(['/menu']);
  }
}
