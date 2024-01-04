package security.spring.service.item;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import security.spring.entity.item.Basket;
import security.spring.entity.item.Book;
import security.spring.entity.item.Item;
import security.spring.entity.item.ItemImg;
import security.spring.entity.member.Member;
import security.spring.exception.ItemNotFoundException;
import security.spring.repository.item.BasketRepository;
import security.spring.repository.item.ItemRepository;
import security.spring.service.member.MemberService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemImgService itemImgService;
    private final MemberService memberService;
    private final BasketRepository basketRepository;

    public Item saveItem(Item item){
        return itemRepository.save(item);
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Optional<Item> findByItemId(Long itemId){
        return itemRepository.findById(itemId);
    }

    public Long saveItem(Item item, List<MultipartFile> itemImgFileList) throws IOException {
        itemRepository.save(item);

        for(int i =0, max = itemImgFileList.size();i<max;i++ ){
            ItemImg itemImg = ItemImg.builder()
                    .item(item)
                    .repimgYn(i == 0 ? "Y" : "N")
                    .build();

            itemImgService.saveItemImg(itemImg, itemImgFileList.get(i));
        }

        return item.getId();
    }

    public Item findByItemName(String itemName){
        return  itemRepository.findByItemName(itemName);
    }
    public Page<Item> findAllPage(Pageable pageable){
        return  itemRepository.findAll(pageable);
    }


    public void bookmark(String itemName) {
        Item byItemName = itemRepository.findByItemName(itemName);
        List<Member> all = memberService.findAll();
        int count = all.get(0).getCount();
        int recommend = byItemName.getRecommend();
        if (count == 1) {
            byItemName.setRecommend(recommend + 1);
            all.set(0, all.get(0)).setCount(0);
            itemRepository.save(byItemName);
            memberService.save(all.get(0));
        }
        if(recommend ==1){
            byItemName.setRecommend(recommend -1);
            all.set(0, all.get(0)).setCount(1);
            itemRepository.save(byItemName);
            memberService.save(all.get(0));
        }
    }

    public Page<Item> search(String itemName, String detail, Pageable pageable){
        return itemRepository.findByItemNameContainingOrAndDetailInfoContaining(itemName,detail,pageable);
    }

    public void updateItem(Item existingItem) {
        itemRepository.save(existingItem);
    }

    public void deleteItem(Long itemId) {
        Optional<Item> byId = itemRepository.findById(itemId);

            if (byId.isPresent()) {
                Item item = byId.get();
                List<ItemImg> files = item.getFiles();

                if (!files.isEmpty()) {
                    List<String> imgs = new ArrayList<>();
                    for (ItemImg file : files) {
                        if(!file.getImgName().isEmpty()) {
                            String imgName = file.getImgName();
                            imgs.add(imgName);
                        }
                    }

                    itemImgService.deleteImg(imgs);
                }

                Basket byBaskItem = basketRepository.findByBaskItem(item);

                if (byBaskItem != null && byBaskItem.getId() != null) {
                    basketRepository.deleteById(byBaskItem.getId());
                }

                itemRepository.deleteByItemName(item.getItemName());
            }
        }
    }

