package th.co.infinitait.comvisitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import th.co.infinitait.comvisitor.entity.CompanyContractEntity;

import java.util.Optional;

public interface CompanyContractRepository extends JpaRepository<CompanyContractEntity, Long> {

    Optional<CompanyContractEntity> findByNameAndCustomerId(@Param("name")String name,@Param("customerId")Long customerId);

}
