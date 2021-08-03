package th.co.infinitait.comvisitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import th.co.infinitait.comvisitor.entity.CardTypeEntity;

import java.util.Optional;

public interface CardTypeRepository extends JpaRepository<CardTypeEntity, Long> {

    Optional<CardTypeEntity> findByNameAndCustomerId(@Param("name")String name,@Param("customerId")Long customerId);

}
