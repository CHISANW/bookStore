package security.spring.entity.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import security.spring.entity.item.Item;

import javax.persistence.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemImg {

    @Id@GeneratedValue
    private Long id;

    private String imgName;
    private String oriImgName;
    private String imgUrl;
    private String repimgYn;

    @ManyToOne
    @JoinColumn(name = "item_Id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgName, String imgUrl) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }
}
