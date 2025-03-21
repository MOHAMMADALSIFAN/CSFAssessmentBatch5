  package vttp.batch5.csf.assessment.server.services;

  import java.util.List;

  import org.springframework.beans.factory.annotation.Autowired;
  import org.springframework.stereotype.Service;

  import vttp.batch5.csf.assessment.server.model.MenuItem;
  import vttp.batch5.csf.assessment.server.model.Order;
  import vttp.batch5.csf.assessment.server.repositories.OrdersRepository;

  @Service
  public class RestaurantService {

    @Autowired
    private OrdersRepository orderRepo;

    // TODO: Task 2.2
    // You may change the method's signature
    public List<MenuItem> getMenu() {
      return orderRepo.getMenu();
    }
    
    // TODO: Task 4
    public void saveOrder(Order order) {
      orderRepo.saveOrder(order);
    }

  }
