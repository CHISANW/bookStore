package security.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import security.spring.dto.ItemDto;
import security.spring.dto.order.DeliveryDto;
import security.spring.entity.item.Item;
import security.spring.entity.item.order.Delivery;
import security.spring.entity.item.order.Order;
import security.spring.entity.item.order.OrderItem;
import security.spring.entity.member.Member;
import security.spring.repository.item.DeliveryRepository;
import security.spring.repository.item.OrderItemRepository;
import security.spring.service.item.BasketService;
import security.spring.service.item.ItemService;
import security.spring.service.item.OrderService;
import security.spring.service.member.MemberService;

import javax.websocket.Session;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class OrderController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderItemRepository orderItemRepository;
    private final BasketService basketService;

    @GetMapping("/order")
    public String order(@RequestParam("itemId") Long itemId, @RequestParam(value = "quantity", required = false, defaultValue = "1") int quantity, Model model){
        Item item = itemService.findByItemId(itemId).get();
        model.addAttribute("delivery", new DeliveryDto());
        model.addAttribute("item",item);
        model.addAttribute("quantity",quantity);

        securityLogin(model);
        return "order/orderItem";
    }

    @GetMapping("/cart/order")
    public String order(@RequestParam("itemIds")List<Long> itemIds, Model model){
        securityLogin(model);
        model.addAttribute("delivery", new DeliveryDto());

        List<Item> items= new ArrayList<>();
        for(Long id: itemIds){
            Item item = itemService.findByItemId(id).get();
            items.add(item);
        }

        model.addAttribute("items",items);

        return "order/orderCartItem";
    }


    @PostMapping("/changePhone")
    @ResponseBody
    public ResponseEntity<String> updatePhone(@RequestParam("memberId")Long memberId, @RequestParam("phoneNumber")String phoneNumber){
        try {
            Member member = memberService.findByPhoneNumber(phoneNumber); // 이미 사용 중인 번호인지 확인
            if (member != null && member.getPhoneNumber().equals(phoneNumber)) {
                return ResponseEntity.badRequest().body("이미 사용 중인 번호입니다.");
            } else {
                Member targetMember = memberService.findById(memberId);
                if (targetMember != null) {
                    targetMember.setPhoneNumber(phoneNumber);
                    memberService.save(targetMember);
                    return ResponseEntity.ok("전화번호 업데이트");
                } else {
                    return ResponseEntity.badRequest().body("해당 멤버를 찾을 수 없습니다.");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("업데이트 중 에러 발생");
        }
    }

    @PostMapping("/orders")
    @ResponseBody
    public ResponseEntity<String> saveRequest(@RequestParam("itemId")Long itemId,
                                              @RequestParam("deliveryRequest")String delivery,
                                              @RequestParam("memberId")Long memberId,
                                              @RequestParam("quantity")int quantity){
       try{
           orderService.saveOrder(memberId,itemId,quantity,delivery);
           return ResponseEntity.ok("상품 주문 성공");
       }catch (Exception e){
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("주문 도중 에러 발생");
       }
    }


    @PostMapping("/orders/cart")
    @ResponseBody
    public ResponseEntity<String> saveCartRequest(@RequestBody Map<String, Object> requestData) {
        try {
            List<String> itemNames = (List<String>) requestData.get("itemNames");
            String delivery = (String) requestData.get("deliveryRequest");
            Long memberId = Long.parseLong(requestData.get("memberId").toString());

            List<Item> items = new ArrayList<>();
            for (String name : itemNames) {
                Item item = itemService.findByItemName(name);
                items.add(item);
            }
            log.info("items={}",items);
            orderService.saveOrders(memberId, items, delivery);
            return ResponseEntity.ok("상품 주문 성공");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("주문 도중 에러 발생");
        }
    }
    @GetMapping("/orders/list")
    public String orderList(Model model, Principal principal){
        securityLogin(model);

        List<Order> orderAll = orderService.findOrderAll();
        String name = principal.getName();
        Member byLoginId = memberService.findByLoginId(name);
        if(!orderAll.isEmpty()){
            Long orderByMemberId = orderAll.get(0).getMember().getId();
            if (byLoginId.getId()==orderByMemberId) {
                model.addAttribute("orders", orderAll);
            }
        }
        return "order/orderList";
    }

    @PostMapping("/OrderCancel")
    @ResponseBody
    public ResponseEntity<?> orderCancel(@RequestBody Map<String, Object> orderCancelData){
        try {
            long orderItemId = Long.parseLong(orderCancelData.get("orderItemId").toString());
            orderService.OrderCancel(orderItemId);
            return ResponseEntity.ok("주문 취소 성공");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("주문취소중 오류 발생");
        }
    }

    @PostMapping("/DeleteBasketItem")
    @ResponseBody
    public ResponseEntity<String> deleteItems(@RequestBody Map<String, Object> basketItems){
        try {
            List<String> itemNames = (List<String>) basketItems.get("itemNames");
            List<Item> items = new ArrayList<>();
            for (String itemName : itemNames) {
                Long itemId = Long.parseLong(itemName);
                Optional<Item> byItemId = itemService.findByItemId(itemId);
                if(byItemId.isPresent()){
                    Item item = byItemId.get();
                    items.add(item);
                }else {
                    return ResponseEntity.status(NOT_FOUND).body("ID에 해당하는 상품을 찾을수 없습니다.");
                }
            }
           basketService.deleteBaskets(items);
            return ResponseEntity.ok("삭제성공");
        }catch (NumberFormatException e){
            e.printStackTrace();
            return ResponseEntity.status(BAD_REQUEST).body("잘못된 ID 형식입니다.");
        }
        catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("장바구니 상품삭제 오류발생");
        }
        
    }

    private void securityLogin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof User){
            User user = (User) authentication.getPrincipal();
            Member byUsername = memberService.findByLoginId(user.getUsername());
            model.addAttribute("userName",byUsername);
        }
    }
}
