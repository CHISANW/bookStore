package security.spring.entity.item;

import lombok.Getter;
import lombok.Setter;
import security.spring.entity.member.Member;

import javax.persistence.*;

@Entity
@Getter@Setter
public class Basket {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_Id")
    private Long id;

    private boolean basketCheck;   // 장바구니 활성 여부
    private int quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id")
    private Member baskMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_Id")
    private Item baskItem;

    public String getItemName(){
        return baskItem.getItemName();
    }
}
