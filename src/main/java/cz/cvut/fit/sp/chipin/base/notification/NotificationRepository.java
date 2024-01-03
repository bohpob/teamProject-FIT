package cz.cvut.fit.sp.chipin.base.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByIdAndUserId(Long id, String userId);
    Page<Notification> findNotificationByUserId(String userId, Pageable pageable);
}
