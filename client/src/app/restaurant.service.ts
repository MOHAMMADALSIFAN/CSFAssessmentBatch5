import { HttpClient } from "@angular/common/http";
import { inject, Injectable } from "@angular/core";
import { catchError, firstValueFrom, Observable, throwError } from "rxjs";
import { CartItem, MenuItem } from "./models";

@Injectable({
  providedIn:'root'
})

export class RestaurantService {

  private API_URL = '/api/menu';
  private API_ORDER_URL = '/api/food-order';
  private http = inject(HttpClient);
  private cartItems: CartItem[] = [];
  
  // TODO: Task 2.2
  // You change the method's signature but not the name
  async getMenuItems(): Promise<MenuItem[]> {
    console.log('Fetching menu items from:', this.API_URL);
    try {
      const menuItems = await firstValueFrom(this.http.get<MenuItem[]>(this.API_URL));
      console.log('Successfully items:', menuItems);
      return menuItems;
    } catch (error) {
      console.error('Error menu items:', error);
      throw error;
    }
  }

  // TODO: Task 3.2 & Task 4
  addToCart(item: CartItem): void {
    const existingItem = this.cartItems.find(i => i._id === item._id);
    if (existingItem) {
      existingItem.quantity = item.quantity;
    } else {
      this.cartItems.push({...item});
    }
  }
  setCartItems(items: CartItem[]): void {
    this.cartItems = [...items];
  }
  getCartItems(): CartItem[] {
    return [...this.cartItems];
  }
  clearCart(): void {
    this.cartItems = [];
  }

  placeOrder(orderData: any): Observable<any> {
    const formattedOrder = {
      username: orderData.username,
      password: orderData.password,
      items: orderData.cartItems.map((item: CartItem) => ({
        _id: item._id,
        name: item.name,
        price: item.price,
        quantity: item.quantity
      }))
    };
    
    return this.http.post(this.API_ORDER_URL, formattedOrder)
      .pipe(
        catchError(error => {
          console.error('Order submission error:', error);
          return throwError(() => error);
        })
      );
  }
}