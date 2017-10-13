package lexisnexis.hackathon.application.repository;

import lexisnexis.hackathon.application.domain.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserNotificationRepository extends JpaRepository<Notification, Long> {

    Optional<Notification> findOneByNotificationId(String notificationId);

}
