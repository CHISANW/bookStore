package security.spring.entity.member;

import lombok.*;
import security.spring.entity.item.Basket;
import security.spring.entity.item.Item;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "member_Id")
    private Long id;

    private String loginId;     //로그인 아이디   

    private String username;       //사용자이름
        
    private String password;        //비밀번호

    private String email;

    private int year;
    private int month;
    private int day;
    private int count;
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    private boolean verified;
    private boolean locked;
    @Column(name = "ACC_CRED_EXPIRED")
    private boolean accountCredentialsExpired;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Item> lists = new ArrayList<>();

    @OneToMany(mappedBy ="baskMember")
    @Builder.Default
    private List<Basket> baskets = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Address> addresses = new ArrayList<>();
}
