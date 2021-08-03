package th.co.infinitait.comvisitor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import th.co.infinitait.comvisitor.entity.FileManagerEntity;

public interface FileManagerRepository extends JpaRepository<FileManagerEntity, Long> {


}
