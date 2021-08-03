package th.co.infinitait.comvisitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import th.co.infinitait.comvisitor.entity.DepartmentEntity;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    Optional<DepartmentEntity> findByNameAndCustomerId(@Param("name")String name,@Param("customerId")Long customerId);

}
