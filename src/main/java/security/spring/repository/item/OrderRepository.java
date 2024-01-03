package security.spring.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.spring.entity.item.order.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

}
