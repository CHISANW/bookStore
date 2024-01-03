//package security.spring.exception;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.ui.Model;
//import org.springframework.validation.BindingResult;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MaxUploadSizeExceededException;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//import security.spring.dto.ItemDto;
//import security.spring.entity.item.Item;
//
//import java.io.IOException;
//
//public class FileException{
//
//    private static final Logger logger = LoggerFactory.getLogger(FileException.class);
//
//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE) // 상태 코드 설정 (예: 파일 크기 초과 시 413 상태 코드 반환)
//    public String handleMaxSizeException(MaxUploadSizeExceededException exc, Model  model) {
//        logger.error("파일 허용 범위를 초과했습니다.", exc);
//        model.addAttribute("errorMessage","파일 허용 범위를 초과했습니다.");
//        model.addAttribute("Item",new ItemDto());
//        return "item/createItem"; // 파일 범위 초과 시 보여줄 에러 페이지 경로
//    }
//}
