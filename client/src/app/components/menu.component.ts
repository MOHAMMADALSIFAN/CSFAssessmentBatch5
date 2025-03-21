import { Component, inject, OnInit } from '@angular/core';
import { CartItem, MenuItem } from '../models';
import { RestaurantService } from '../restaurant.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css',
})
export class MenuComponent implements OnInit {
  menuItems: MenuItem[] = [];
  cart: CartItem[] = [];
  totalItems: number = 0;
  totalPrice: number = 0;

  private restaurantSvc = inject(RestaurantService)
  private router = inject(Router)

  ngOnInit(): void {
    this.loadMenuItems();
  }

  async loadMenuItems(): Promise<void> {
    console.log('load from mongodb menu items');
    
    try {
      this.menuItems = await this.restaurantSvc.getMenuItems();
      console.log('Menu items loaded successfully from mongodbtofrontend:', this.menuItems);
    } catch (err) {
      console.error('Error loading menu items:', err);
    }
  }

  addToCart(item: MenuItem): void {
    console.log('Adding item to cart:', item.name);
    const existingItem = this.cart.find(cartItem => cartItem._id === item._id);
    if (existingItem) {
      existingItem.quantity += 1;
    } else {
      this.cart.push({ ...item, quantity: 1 });
    }
    this.updateCartTotals();
  }

  removeFromCart(item: MenuItem): void {
    console.log('Removing item from cart:', item.name);
    const existingItem = this.cart.find(cartItem => cartItem._id === item._id);
    if (existingItem && existingItem.quantity > 1) {
      existingItem.quantity -= 1;
    } else if (existingItem) {
      this.cart = this.cart.filter(cartItem => cartItem._id !== item._id);
    }
    this.updateCartTotals();
  }

  getItemQuantity(itemId: string): number {
    const item = this.cart.find(cartItem => cartItem._id === itemId);
    return item ? item.quantity : 0;
  }

  updateCartTotals(): void {
    this.totalItems = this.cart.reduce((total, item) => total + item.quantity, 0);
    this.totalPrice = this.cart.reduce((total, item) => total + (item.price * item.quantity), 0);
    console.log('Cart updated - Total items:', this.totalItems, 'Total price:', this.totalPrice);
  }

  hasItemInCart(itemId: string): boolean {
    return this.cart.some(item => item._id === itemId);
  }

  placeOrder(): void {
    this.restaurantSvc.setCartItems(this.cart); 
    this.router.navigate(['/place-order']);
  }
}

