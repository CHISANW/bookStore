package security.spring.entity.item.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import security.spring.entity.item.Item;

import javax.persistence.*;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_Item")
@Data
public class OrderItem {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderItem_Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_Id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_Id")
    private Order order;

    private int orderPrice;     //상품가격  
    private int count;          // 갯수
    private Integer totalPrice; //총 상품가격

    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        return orderItem;
    }

}
