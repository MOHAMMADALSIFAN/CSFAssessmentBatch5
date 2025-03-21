package vttp.batch5.csf.assessment.server.repositories;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import vttp.batch5.csf.assessment.server.model.MenuItem;
import vttp.batch5.csf.assessment.server.model.Order;

@Repository
public class OrdersRepository {

    @Autowired
    private MongoTemplate template;

    // TODO: Task 2.2
    // You may change the method's signature
    // Write the native MongoDB query in the comment below
    //  db.menus.find().sort({name:-1})
    public List<MenuItem> getMenu() {
        Query query = new Query();
        query.with(Sort.by(Sort.Direction.ASC, "name"));
        List<MenuItem> menuItems = template.find(query, MenuItem.class, "menus");
        return menuItems;
    }

    // TODO: Task 4
    // Write the native MongoDB query for your access methods in the comment below
//   db.orders.insert({
//   "_id": "3fe2e3dc",
//   "order_id": "3fe2e3dc",
//   "payment_id": "01JPVH8GGHR8DCMXN9YCSCTMPA",
//   "username": "fred",
//   "total": 24.60,
//   "timestamp": ISODate("2025-03-21T05:01:53.767Z"),
//   "items": [
//     {
//       "_id": "9aedc2a8",
//       "name": "Balik Ekmek",
//       "price": 9.2,
//       "quantity": 1
//     },
//     {
//       "_id": "b9f0f5e1",
//       "name": "Chicken Bruschetta",
//       "price": 7.7,
//       "quantity": 2
//     }
//   ]
// })
    //  Native MongoDB query here
    public void saveOrder(Order order) {
        template.insert(order, "orders");
    }
}
