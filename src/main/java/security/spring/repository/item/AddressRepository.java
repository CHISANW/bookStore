package security.spring.repository.item;

import org.springframework.data.jpa.repository.JpaRepository;
import security.spring.entity.member.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {
}
