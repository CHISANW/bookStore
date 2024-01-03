package security.spring.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import security.spring.entity.item.Book;
import security.spring.entity.item.Item;
import security.spring.entity.item.ItemSellStatus;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {

    private Long id;

    @NotBlank
    private String author;
    private String isbn;
    private String itemName;
    private int stockQuantity;

    private ItemSellStatus itemSellStatus;

    private int price;
    private int deliveryPrice; // 배송비


    private String detailInfo;

    @Builder.Default
    private List<ItemImgDto> itemImgDtoList = new ArrayList<>();
    @Builder.Default
    private List<Long> itemImgIds = new ArrayList<>();

//    @Builder
//    public ItemDto(Long itemId,String itemName, int price, int stockQuantity, String author, int  deliveryPrice,String isbn,
//                   String detailInfo, ItemSellStatus itemSellStatus) {
//        this.id=itemId;
//        this.itemName = itemName;
//        this.price = price;
//        this.stockQuantity = stockQuantity;
//        this.deliveryPrice = deliveryPrice;
//        this.author = author;
//        this.isbn = isbn;
//        this.detailInfo = detailInfo;
//        this.itemSellStatus = itemSellStatus;
//    }

    public Item toEntity(ItemDto dto){
        Book entity = Book.builder()
                .id(dto.id)
                .name(dto.itemName)
                .price(dto.price)
                .stockQuantity(dto.stockQuantity)
                .author(dto.author)
                .deliveryPrice(dto.deliveryPrice)
                .isbn(dto.isbn)
                .itemDetail(dto.detailInfo)
                .itemSellStatus(dto.itemSellStatus)
                .build();

        return entity;
    }

    public ItemDto ofDto(Book book){
        ItemDto entity = ItemDto.builder()
                .itemName(book.getItemName())
                .price(book.getPrice())
                .stockQuantity(book.getStockQuantity())
                .author(book.getAuthor())
                .deliveryPrice(book.getDeliveryPrice())
                .isbn(book.getIsbn())
                .detailInfo(book.getDetailInfo())
                .itemSellStatus(book.getItemSellStatus()).build();

        return entity;
    }
}
