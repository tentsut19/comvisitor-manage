package th.co.infinitait.comvisitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import th.co.infinitait.comvisitor.entity.CardRegisterEntity;

import java.util.List;
import java.util.Optional;

public interface CardRegisterRepository extends JpaRepository<CardRegisterEntity, Long> {

    @Query(value = "SELECT * FROM tb_approve_group " +
            "WHERE personnel_id = :personnelId ", nativeQuery = true)
    Optional<CardRegisterEntity> findByPersonnelId(@Param("personnelId")Long personnelId);

    Optional<CardRegisterEntity> findByFirstNameAndLastNameAndCustomerId(
            @Param("firstName")String firstName
            ,@Param("lastName")String lastName
            ,@Param("customerId")Long customerId);

    List<CardRegisterEntity> findByCodeAndCustomerId(@Param("code")String code, @Param("customerId")Long customerId);

}
