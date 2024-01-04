package security.spring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import security.spring.entity.item.Basket;
import security.spring.service.item.BasketService;

import java.util.Map;


@Controller
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;

    @PostMapping("/basket/v1")
    @ResponseBody
    public boolean saveBasket(@RequestParam("memberLoginId") String loginId, @RequestParam("ItemName") String ItemName, Model model){
        boolean basket = basketService.saveBasket(loginId, ItemName);
        return basket;
    }

    @PostMapping("/basket")
    @ResponseBody
    public ResponseEntity<?> saveBas(@RequestBody Map<String, Object> requestData){

        try {
            String loginId = (String) requestData.get("loginId");
            long itemId = Long.parseLong(requestData.get("itemId").toString());
            basketService.saveBaskets(loginId, itemId);
            return  ResponseEntity.ok("장바구니등록 성공");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류");
        }
    }

    @GetMapping("/baskets")
    public Iterable<Basket> getAllBaskets(){
        return basketService.getAll();
    }

}
