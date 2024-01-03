package security.spring.repository.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import security.spring.entity.item.Book;
import security.spring.entity.item.Item;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Page<Item>  findAll(Pageable pageable);
    void deleteByItemName(String itemName);
    Item findByItemName(String itemName);
//    void deleteByitem_Id(Long itemId);

    Page<Item> findByItemNameContainingOrAndDetailInfoContaining(String itemName, String detail, Pageable pageable);
//    int findByBookmark();

}
