package lexisnexis.hackathon.application.service;

import lexisnexis.hackathon.application.domain.Notification;
import lexisnexis.hackathon.application.repository.UserNotificationRepository;
import lexisnexis.hackathon.application.service.dto.NotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class NotifictaionService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserNotificationRepository userNotificationRepository;

    public NotifictaionService(UserNotificationRepository userNotificationRepository){
        this.userNotificationRepository = userNotificationRepository;
    }

    public Notification createNotification(NotificationDTO notificationDTO){
        Notification notification = new Notification();
        notification.setBody(notificationDTO.getBody());
        notification.setData(notificationDTO.getData());
        notification.setIcon(notificationDTO.getIcon());
        notification.setLink(notificationDTO.getLink());
        notification.setRequireInteraction(notificationDTO.getRequireInteraction());
        notification.setSendTime(notificationDTO.getSendTime());
        notification.setSendTo(notificationDTO.getSendTo());
        notification.setSent(notificationDTO.getSent());
        notification.setTag(notificationDTO.getTag());
        notification.setSilent(notificationDTO.getSilent());
        userNotificationRepository.save(notification);
        return notification;
    }
}
