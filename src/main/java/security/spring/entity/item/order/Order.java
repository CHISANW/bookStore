package security.spring.entity.item.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import security.spring.entity.item.Item;
import security.spring.entity.member.Member;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id")
    private Member member;      //주문회원

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_Id")
    private Delivery delivery;

    private Date orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();

}

