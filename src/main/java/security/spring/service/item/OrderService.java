package security.spring.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.spring.dto.order.DeliveryDto;
import security.spring.entity.item.Item;
import security.spring.entity.item.order.Delivery;
import security.spring.entity.item.order.Order;
import security.spring.entity.item.order.OrderItem;
import security.spring.entity.item.order.OrderStatus;
import security.spring.entity.member.Member;
import security.spring.repository.item.DeliveryRepository;
import security.spring.repository.item.OrderItemRepository;
import security.spring.repository.item.OrderRepository;
import security.spring.service.member.MemberService;

import java.util.*;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberService memberService;
    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemService itemService;

    public Order saveOrder(Long memberID, Long itemId,int quantity,String deliveryRequest){
        Date date = new Date();
        List<OrderItem> orderItems = new ArrayList<>();
        Item item = itemService.findByItemId(itemId).get();
        Member member = memberService.findById(memberID);

        OrderItem orderItem = OrderItem.builder()
                .item(item)
                .orderPrice(item.getPrice())
                .count(quantity)
                .totalPrice((item.getPrice()+item.getDeliveryPrice())*quantity)
                .build();

        orderItems.add(orderItem);

        Delivery delivery = Delivery.builder()
                .deliveryRequest(deliveryRequest).build();

        Order order = Order.builder()
                .delivery(delivery)
                .orderItems(orderItems)
                .member(member)
                .status(OrderStatus.주문접수)
                .orderDate(date).build();
        orderItem.setOrder(order);

       return orderRepository.save(order);
    }

    public void saveOrders(Long memberId, List<Item> items,String deliveryRequest) {
        Date date = new Date();
        List<OrderItem> orderItems = new ArrayList<>();
        Member member = memberService.findById(memberId);


        Order order = Order.builder()
                .member(member)
                .status(OrderStatus.주문접수)
                .orderDate(date)
                .delivery(Delivery.builder().deliveryRequest(deliveryRequest).build())
                .build();

        for (Item item : items) {
            // 주문 상품 생성
            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .orderPrice(item.getPrice())
                    .count(item.getBaskets().get(0).getQuantity())
                    .totalPrice((item.getPrice() + item.getDeliveryPrice()) * item.getBaskets().get(0).getQuantity())
                    .order(order) // Order와 OrderItem 간의 관계 설정
                    .build();

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems); // Order와 OrderItem의 관계 설정

        orderRepository.save(order);
    }

    public void OrderCancel(Long orderItemId){

        try {
            OrderItem orderItem = orderItemRepository.findById(orderItemId).get();
            Order order = orderItem.getOrder();
            orderItemRepository.deleteByOrderId(order.getId());
            deliveryRepository.deleteById(order.getDelivery().getId());
            deleteById(order.getId());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public List<Order> findOrderAll(){
        return orderRepository.findAll();
    }

    public Order findById(Long ItemId){
        Order order = orderRepository.findById(ItemId).get();
        return order;
    }


    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }
}
