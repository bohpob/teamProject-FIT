package cz.cvut.fit.sp.chipin.base.notification.content;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationContentRepository extends JpaRepository<NotificationContent, Long> {
    Optional<NotificationContent> findById(Long id);
}

