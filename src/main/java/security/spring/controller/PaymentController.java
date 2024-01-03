package security.spring.controller;

import com.siot.IamportRestClient.IamportClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import security.spring.entity.item.Item;
import security.spring.service.item.ItemService;

import javax.annotation.PostConstruct;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PaymentController {

    private final ItemService itemService;

    private IamportClient iamportClient;

    @Value("${imp.api.key}")
    private String apiKey;

    @Value("${imp.api.secretkey}")
    private String secretKey;

    @PostConstruct
    public void init(){
        this.iamportClient = new IamportClient(apiKey,secretKey);
    }


    @GetMapping("/getItemInfo/")
    @ResponseBody
    public ResponseEntity<Item> getItemInfo(@RequestParam("itemId") Long itemId){
        Item item = itemService.findByItemId(itemId).get();

        if(item!=null){
            return ResponseEntity.ok(item);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

}
