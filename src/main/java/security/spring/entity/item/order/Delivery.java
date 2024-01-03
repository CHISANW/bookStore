package security.spring.entity.item.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_Id")
    private Long id;

    private String deliveryRequest; //배송 요청사항
}
