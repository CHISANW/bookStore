package security.spring.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import security.spring.entity.item.ItemImg;

import java.util.Optional;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    Optional<ItemImg> findById(Long id);
    void deleteByOriImgName(String oriImgName);
}
