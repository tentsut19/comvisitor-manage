package th.co.infinitait.comvisitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.infinitait.comvisitor.entity.CustomerEntity;

public interface CustomerRepository extends JpaRepository<CustomerEntity, Long> {

}
