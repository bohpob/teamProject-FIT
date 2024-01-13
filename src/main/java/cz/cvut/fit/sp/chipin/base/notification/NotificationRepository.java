package cz.cvut.fit.sp.chipin.base.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Optional<Notification> findByIdAndUserId(Long id, String userId);

    List<Notification> findNotificationsByUserId(String userId);
    Page<Notification> findNotificationsByUserId(String userId, Pageable pageable);
}
