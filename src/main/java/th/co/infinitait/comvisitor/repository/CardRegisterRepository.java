package th.co.infinitait.comvisitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import th.co.infinitait.comvisitor.entity.CardRegisterEntity;

import java.util.List;

public interface CardRegisterRepository extends JpaRepository<CardRegisterEntity, Long> {

    @Query(value = "SELECT * FROM tb_activity WHERE department_id = :departmentId", nativeQuery = true)
    List<CardRegisterEntity> findByDepartmentId(@Param("departmentId") long departmentId);

}
