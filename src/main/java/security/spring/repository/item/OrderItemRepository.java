package security.spring.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import security.spring.entity.item.order.OrderItem;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    void deleteByOrderId(Long orderId);

}
