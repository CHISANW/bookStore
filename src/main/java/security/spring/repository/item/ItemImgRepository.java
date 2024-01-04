package security.spring.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import security.spring.entity.item.ItemImg;

import java.util.Optional;

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    Optional<ItemImg> findById(Long id);
    ItemImg findByOriImgName(String oriImgName);
    void deleteByOriImgName(String oriImgName);

    ItemImg findByImgName(String imgName);

    @Modifying
    @Query("DELETE FROM ItemImg img WHERE img.item.id = :itemId")
    void deleteByItemId(@Param("itemId") Long itemId);
}
