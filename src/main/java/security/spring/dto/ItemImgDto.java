package security.spring.dto;

import lombok.Builder;
import lombok.Data;
import security.spring.entity.item.ItemImg;

@Data
@Builder
public class ItemImgDto {

    private Long id;
    private String imgName;
    private String originalName;
    private String imgUrl;
    private String repImgTn;



    public ItemImg toEntity(ItemImgDto dto){
        ItemImg entity = ItemImg.builder()
                .imgName(dto.imgName)
                .imgUrl(dto.imgUrl)
                .oriImgName(dto.originalName)
                .repimgYn(dto.originalName)
                .build();

        return entity;
    }
}
