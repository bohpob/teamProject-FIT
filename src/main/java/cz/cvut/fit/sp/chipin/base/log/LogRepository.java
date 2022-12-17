package cz.cvut.fit.sp.chipin.base.log;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    ArrayList<Log> findAllByGroupId(Long groupId);
}