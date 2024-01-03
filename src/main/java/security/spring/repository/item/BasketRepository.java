package security.spring.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.spring.entity.item.Basket;
import security.spring.entity.item.Item;
import security.spring.entity.member.Member;

import java.util.Optional;

@Repository
public interface BasketRepository extends JpaRepository<Basket,Long> {
        Basket findByBaskMemberAndBaskItem(Member member, Item item);
        Optional<Basket> findById(Long id);
        Basket findByBaskItem(Item item);


}
