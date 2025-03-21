// You may use this file to create any models
export interface MenuItem {
  _id: string;
  name: string;
  price: number;
  description: string;
}

export interface CartItem extends MenuItem {
  quantity: number;
}