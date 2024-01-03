package security.spring.entity.item;

import lombok.Data;
import lombok.NoArgsConstructor;
import security.spring.entity.item.order.Order;
import security.spring.entity.item.order.OrderItem;
import security.spring.entity.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "Items")
public abstract class Item {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_Id")
    private Long Id;

    private String itemName;

    private Integer price;

    private Integer itemQuantity;


    private int deliveryPrice; // 배송비

    private int stockQuantity;

    @Column(length = 10000)
    private String detailInfo;


    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int recommend;

    @OneToMany(mappedBy = "baskItem" )
    private List<Basket> baskets = new ArrayList<>();

    @OneToMany(mappedBy = "item" ,cascade = CascadeType.ALL)
    private List<ItemImg> files = new ArrayList<>();


    public void saveItem(Long id,String itemName, int price, String detailInfo, int stockQuantity,ItemSellStatus itemSellStatus,int deliveryPrice){
        this.Id = id;
        this.itemName = itemName;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.detailInfo = detailInfo;
        this.itemSellStatus = itemSellStatus;
        this.deliveryPrice= deliveryPrice;
        this.recommend =0;
    }



}
