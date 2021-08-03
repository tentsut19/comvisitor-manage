package th.co.infinitait.comvisitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import th.co.infinitait.comvisitor.entity.CardVisitorTemplateEntity;

import java.util.Optional;

public interface CardVisitorTemplateRepository extends JpaRepository<CardVisitorTemplateEntity, Long> {

    Optional<CardVisitorTemplateEntity> findByIdAndCustomerId(@Param("cardVisitorTemplateId")Long cardVisitorTemplateId,@Param("customerId")Long customerId);

}
