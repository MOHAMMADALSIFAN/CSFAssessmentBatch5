import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { CartItem } from '../models';
import { RestaurantService } from '../restaurant.service';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit {
  cartItems: CartItem[] = [];
  totalPrice: number = 0;
  form!: FormGroup;
  isSubmitting: boolean = false;
  
  constructor(
    private fb: FormBuilder,
    private restaurantSvc: RestaurantService,
    private router: Router
  ) {
    this.form = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.cartItems = this.restaurantSvc.getCartItems();
    this.calculateTotal();
    if (this.cartItems.length === 0) {
      this.router.navigate(['/menu']);
    }
  }
  
  calculateTotal(): void {
    this.totalPrice = this.cartItems.reduce(
      (total, item) => total + (item.price * item.quantity), 0
    );
  }
  
  getItemSubtotal(item: CartItem): number {
    return item.price * item.quantity;
  }
  
  confirmOrder(): void {
    if (this.form.valid) {
      const orderData = {
        username: this.form.get('username')?.value,
        password: this.form.get('password')?.value,
        cartItems: this.cartItems,
        totalPrice: this.totalPrice
      };
      
      this.restaurantSvc.placeOrder(orderData).subscribe({
        next: (response) => {
          console.log('Order placed successfully', response);
          let orderData = response;
          if (typeof response === 'string') {
            try {
              orderData = JSON.parse(response);
            } catch (e) {
              console.error('Error parsing response', e);
            }
          }
          this.restaurantSvc.clearCart();
          this.router.navigate(['/confirmation'], { state: { orderData } });
          
          this.isSubmitting = false;
        },
        error: (error) => {
          this.isSubmitting = false;
          console.error('Error placing order', error);
          let errorMessage = 'An error occurred while placing your order';
          if (error.error && typeof error.error === 'string') {
            try {
              const errorObj = JSON.parse(error.error);
              errorMessage = errorObj.message || errorMessage;
            } catch (e) {
              console.error('Error parsing error response', e);
            }
          } else if (error.error && error.error.message) {
            errorMessage = error.error.message;
          }
          alert(errorMessage);
        }
      });
    }
  }
  startOver(): void {
    this.restaurantSvc.clearCart();
    this.router.navigate(['/menu']);
  }
}