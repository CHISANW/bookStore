package security.spring.service.item;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;
import security.spring.entity.item.ItemImg;
import security.spring.repository.item.ItemImgRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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

    public void updateImg(List<ItemImg> files, List<MultipartFile> multipartFiles) {
        try {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile multipartFile = multipartFiles.get(i);
                String imgName = "";
                String imgUrl = "";
                String originalFilename = multipartFile.getOriginalFilename();
                byte[] bytes = multipartFile.getBytes();

                if (!StringUtils.isEmpty(originalFilename)) {
                    imgName = fileService.uploadFile(itemImgLocation, originalFilename, bytes);
                    imgUrl = "/uploads/" + imgName;
                }

                ItemImg itemImg = files.get(i);
                String imgImgName = itemImg.getImgName();
                // 변경된 파일에 대해서만 업데이트 수행
                if (!StringUtils.isEmpty(imgName)) {
                    itemImg.updateItemImg(originalFilename, imgName, imgUrl);
                    itemImgRepository.save(itemImg);
                    File file = new File(itemImgLocation, imgImgName);
                    file.delete();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteImg(List<String> imgNames){
        log.info("IMAGES={}",imgNames);
        for (String imgName : imgNames) {
            File file = new File(itemImgLocation, imgName);
            log.info("imgName={}",imgName);
            file.delete();
            itemImgRepository.deleteByOriImgName(imgName);
        }
    }

}
