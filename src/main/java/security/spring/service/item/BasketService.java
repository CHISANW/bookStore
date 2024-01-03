package security.spring.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.spring.entity.item.Basket;
import security.spring.entity.item.Item;
import security.spring.entity.item.order.OrderItem;
import security.spring.entity.member.Member;
import security.spring.repository.item.BasketRepository;
import security.spring.repository.item.OrderItemRepository;
import security.spring.service.member.MemberService;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BasketService {

    private final BasketRepository basketRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    public Basket save(Basket basket){
        return basketRepository.save(basket);
    }

    public boolean saveBasket(String memberLoginId, String itemName){
        if (memberLoginId.isEmpty() || itemName.isEmpty()) {
            return false;
        }

        Member member = memberService.findByLoginId(memberLoginId);
        Item item = itemService.findByItemName(itemName);

        // 장바구니에 해당 아이템이 이미 들어있는지 확인
        Basket existingBasketItem = basketRepository.findByBaskMemberAndBaskItem(member, item);
        if (existingBasketItem != null) {
            // 이미 장바구니에 있는 경우 처리
            int newPrice = existingBasketItem.getQuantity() + 1;
            existingBasketItem.setQuantity(newPrice);
            basketRepository.save(existingBasketItem);
        } else {
            // 장바구니에 없는 경우 새로운 아이템으로 추가
            Basket newBasket = new Basket();
            newBasket.setBaskMember(member);
            newBasket.setBaskItem(item);
            newBasket.setQuantity(1);
            newBasket.setBasketCheck(true);
            basketRepository.save(newBasket);
        }
        return true;
    }


    public List<Basket> getAll() {
        return basketRepository.findAll();
    }

    public Basket findItem(Item item){
        return basketRepository.findByBaskItem(item);
    }

    public void saveBaskets(String loginId, Long OrderItemId) {
        if (loginId.isEmpty() || OrderItemId==null ) {
            throw new RuntimeException("사용자나 등록된 상품이 없습니다.");
        }
        Member member = memberService.findByLoginId(loginId);
        OrderItem orderItem = orderItemRepository.findById(OrderItemId).get();
        Item item = orderItem.getItem();
        // 장바구니에 해당 아이템이 이미 들어있는지 확인
        Basket existingBasketItem = basketRepository.findByBaskMemberAndBaskItem(member, item);
        if (existingBasketItem != null) {
            // 이미 장바구니에 있는 경우 처리
            int newPrice = existingBasketItem.getQuantity() + 1;
            existingBasketItem.setQuantity(newPrice);
            basketRepository.save(existingBasketItem);
        } else {
            // 장바구니에 없는 경우 새로운 아이템으로 추가
            Basket newBasket = new Basket();
            newBasket.setBaskMember(member);
            newBasket.setBaskItem(item);
            newBasket.setQuantity(1);
            newBasket.setBasketCheck(true);
            basketRepository.save(newBasket);
        }
    }

    public void deleteBaskets(List<Item> items){
        for (Item item : items) {
            Basket basket = findItem(item);
            basketRepository.deleteById(basket.getId());
        }
    }

//    public void findBasket(Long itemId){
//        Basket byBaskItemId = basketRepository.findByBaskItemId(itemId);
//        basketRepository.delete(byBaskItemId);
//    }
}
