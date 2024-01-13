package cz.cvut.fit.sp.chipin.base.log;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findLogsByGroupId(Long groupId);
    Page<Log> findLogsByGroupId(Long groupId, Pageable pageable);
}