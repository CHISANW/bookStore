package security.spring.service.item;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import security.spring.entity.item.ItemImg;
import security.spring.repository.item.ItemImgRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ItemImgService {

    private final FileService fileService;
    private final ItemImgRepository itemImgRepository;

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    public void saveItemImg(ItemImg itemImg, MultipartFile multipartFile)throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String imgName= "";
        String imgUrl="";

        if(!StringUtils.isEmpty(originalFilename)){
            imgName = fileService.uploadFile(itemImgLocation,originalFilename, multipartFile.getBytes());
            imgUrl = "/uploads/"+imgName;
        }

        itemImg.updateItemImg(originalFilename,imgName,imgUrl);
        itemImgRepository.save(itemImg);
    }

    public Optional<ItemImg> findImg(Long fileId){
        Optional<ItemImg> byId = itemImgRepository.findById(fileId);
        return byId;
    }

    public void deleteImg(String  imgName){
        File file = new File(itemImgLocation,imgName);
        file.delete();
        itemImgRepository.deleteByOriImgName(imgName);
    }

}
