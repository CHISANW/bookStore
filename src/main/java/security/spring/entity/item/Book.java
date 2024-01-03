package security.spring.entity.item;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Book extends Item {

    private String author;
    private String isbn;

    @Builder
    public Book(Long id, String name, int price, int stockQuantity, String itemDetail, ItemSellStatus itemSellStatus, int deliveryPrice, String author, String isbn){
        super.saveItem(id,name,price,itemDetail,stockQuantity,itemSellStatus,deliveryPrice);
        this.author = author;
        this.isbn = isbn;
    }


}
