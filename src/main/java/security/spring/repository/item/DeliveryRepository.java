package security.spring.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.spring.entity.item.order.Delivery;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery,Long> {
}
