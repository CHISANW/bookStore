package security.spring.service.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class FileService {
    public String uploadFile(String uploadPath, String originalFileName, byte[] fileData)throws IOException {
        UUID uuid = UUID.randomUUID();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String saveFileName = uuid.toString()+extension;
        String fileUploadFullUrl = uploadPath+"/"+saveFileName;

        FileOutputStream fos = new FileOutputStream(fileUploadFullUrl);
        fos.write(fileData);
        fos.close();

        return saveFileName;
    }

}
