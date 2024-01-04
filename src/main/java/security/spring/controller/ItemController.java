package security.spring.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import security.spring.dto.ItemDto;
import security.spring.dto.ItemImgDto;
import security.spring.entity.item.Basket;
import security.spring.entity.item.Book;
import security.spring.entity.item.Item;
import security.spring.entity.item.ItemImg;
import security.spring.entity.member.Member;
import security.spring.service.item.BasketService;
import security.spring.service.item.ItemImgService;
import security.spring.service.item.ItemService;
import security.spring.service.member.MemberService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ItemController {

    private final MemberService memberService;
    private final ItemService itemService;
    private final BasketService basketService;
    private final ItemImgService itemImgService;

    @GetMapping("/addItem")
    public String addItem(Model model){
        securityLogin(model);

        model.addAttribute("Item",new ItemDto());
        return "item/createItem";
    }

    @PostMapping("/addItem")
    public String addItemPost(@Valid @ModelAttribute("Item") ItemDto itemDto, BindingResult result,
                              @RequestParam("itemImgFile")List<MultipartFile> itemImgFileList, Model model, Principal principal){

        securityLogin(model);
        if(itemImgFileList.get(0).isEmpty() && itemDto.getId()==null){
            model.addAttribute("errorMessage","첫번째 상품 이미지는 필수입니다.");
            return "item/createItem";
        }
        if(result.hasErrors()){
            return "item/createItem";
        }

        try {
            String name = principal.getName();
            Item entity = itemDto.toEntity(itemDto);
            entity.setMember(memberService.findByLoginId(name));
            itemService.saveItem(entity,itemImgFileList);
        }catch (Exception e){
            model.addAttribute("errorMessage","상품 등록 실패");
            return "item/createItem";
        }
        return "redirect:/";

    }

    @GetMapping("/item/itemAll")
    public String ItemAll(Model model, @PageableDefault(size = 15) Pageable pageable, @RequestParam(required = false, defaultValue = "") String searchText){
        securityLogin(model);

        Page<Item> items = itemService.search(searchText,searchText,pageable);
        model.addAttribute("items",items);

        int startPage = Math.max(1,items.getPageable().getPageNumber()-4);
        int endPage = Math.min(items.getTotalPages(),items.getPageable().getPageNumber()+4);


        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);


        return "item/itemAll";
    }

    @GetMapping("/item/updateAll")
    public String updateAll(Model model){
        securityLogin(model);

        List<Item> items = itemService.findItems();
        model.addAttribute("items",items);
        return "item/updateAll";
    }
    @GetMapping("/item/{itemId}/info")
    public String ItemInfo(@PathVariable("itemId") Long itemId, Model model){

        securityLogin(model);

        Optional<Item> byItemId = itemService.findByItemId(itemId);
        model.addAttribute("book",byItemId.get());

        return "item/itemInfo";
    }

    @GetMapping("/item/basket")
    public String basket(Model model){
        securityLogin(model);

        List<Basket> baskets = basketService.getAll();
        model.addAttribute("baskets",baskets);
        return "item/itemBasket";
    }

    @PostMapping("/item/delete/{id}")
    public String deleteItem(@PathVariable("id")Long itemId){
        log.info("Controller itemId={}",itemId);
        itemService.deleteItem(itemId);
        return "redirect:/";
    }

    @PostMapping("/item/delete")
    @ResponseBody
    public ResponseEntity<?> delete(@RequestBody Map<String, Object> requestData) {
        try {
            if (!requestData.containsKey("itemId")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("itemId가 요청에 포함되지 않았습니다.");
            }

            long itemId;
            try {
                itemId = Long.parseLong(requestData.get("itemId").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 itemId입니다.");
            }
            itemService.deleteItem(itemId);

            return ResponseEntity.ok("삭제 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 중 오류 발생: " + e.getMessage());
        }
    }

    @GetMapping("/item/update/{id}")
    public String updateItem(@PathVariable("id")Long itemId, Model model){

        securityLogin(model); // 사용자 정보 넘기기

        Book book = (Book) itemService.findByItemId(itemId).get();
        ItemDto itemDto = getItemDto(book);

        model.addAttribute("item",itemDto);

        return "item/update-Item";

    }


    @PostMapping("/item/update/{id}")
    public String updatePostItem(@PathVariable("id") Long itemId, @ModelAttribute("item") ItemDto updatedItemDto,
                                 @RequestParam("itemImgFile") List<MultipartFile> updatedItemImgFileList, Model model) {

        try {
            Optional<Item> optionalItem = itemService.findByItemId(itemId);
            if (optionalItem.isPresent()) {
                Book existingItem = (Book) optionalItem.get();

                // 업데이트된 정보로 기존 ItemDto 업데이트
                existingItem.setItemName(updatedItemDto.getItemName());
                existingItem.setPrice(updatedItemDto.getPrice());
                existingItem.setStockQuantity(updatedItemDto.getStockQuantity());
                existingItem.setDeliveryPrice(updatedItemDto.getDeliveryPrice());
                existingItem.setAuthor(updatedItemDto.getAuthor());
                existingItem.setIsbn(updatedItemDto.getIsbn());
                existingItem.setDetailInfo(updatedItemDto.getDetailInfo());
                existingItem.setItemSellStatus(updatedItemDto.getItemSellStatus());
                List<ItemImg> files = existingItem.getFiles();
                itemService.updateItem(existingItem);
                itemImgService.updateImg(files,updatedItemImgFileList);

            } else {
                model.addAttribute("errorMessage", "해당 상품을 찾을 수 없습니다.");
                return "item/update-Item"; // 오류 처리
            }
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 업데이트 실패");
            return "item/update-Item"; // 오류 처리
        }

        return "redirect:/"; // 성공 시 상품 정보 페이지로 이동
    }

    @PostMapping("/updateQuantity")
    @ResponseBody
    public ResponseEntity<String> updateQuantity(@RequestParam("itemId")Long itemId, @RequestParam("quantity")int quantity){
        try{
            Item item = itemService.findByItemId(itemId).get();
            if(item!=null){
                Basket basket = basketService.findItem(item);
                basket.setQuantity(quantity);
                basketService.save(basket);
                
                return ResponseEntity.ok("수량 업데이트");
            }else{
                return ResponseEntity.badRequest().body("아이템을 찾을수가 없습니다.");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("업데이트중 에러 발생");
        }
    }

    private void securityLogin(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getPrincipal() instanceof  User){
            User user = (User) authentication.getPrincipal();
            Member byUsername = memberService.findByLoginId(user.getUsername());
            model.addAttribute("userName",byUsername);
        }
    }

    private List<ItemImgDto> getItemImgDtoList(List<ItemImg> itemImgList){
        List<ItemImgDto> itemImgDtoList = new ArrayList<>();
        for (ItemImg itemImg : itemImgList) {
            ItemImgDto build = ItemImgDto.builder()
                    .id(itemImg.getId())
                    .originalName(itemImg.getOriImgName())
                    .repImgTn(itemImg.getRepimgYn())
                    .imgUrl(itemImg.getImgUrl())
                    .imgName(itemImg.getImgName()).build();
            itemImgDtoList.add(build);
        }
        return itemImgDtoList;
    }

    private ItemDto getItemDto(Book book) {
        ItemDto itemDto = ItemDto.builder()
                .id(book.getId())
                .itemName(book.getItemName())
                .price(book.getPrice())
                .stockQuantity(book.getStockQuantity())
                .author(book.getAuthor())
                .deliveryPrice(book.getDeliveryPrice())
                .isbn(book.getIsbn())
                .detailInfo(book.getDetailInfo())
                .itemSellStatus(book.getItemSellStatus())
                .itemImgDtoList(getItemImgDtoList(book.getFiles()))
                .build();
        return itemDto;
    }



}
