package lexisnexis.hackathon.application.web.rest.notification;

import lexisnexis.hackathon.application.domain.Notification;
import lexisnexis.hackathon.application.service.NotifictaionService;
import lexisnexis.hackathon.application.service.dto.NotificationDTO;
import lexisnexis.hackathon.application.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;


@RestController
@RequestMapping("/api")
public class NotificationController {

    private final Logger log = LoggerFactory.getLogger(lexisnexis.hackathon.application.web.rest.UserResource.class);

    private final NotifictaionService notifictaionService;

    public NotificationController(NotifictaionService notifictaionService) {
        this.notifictaionService = notifictaionService;
    }

    @PostMapping("/createNotification")
    public ResponseEntity createNotification(@Valid @RequestBody NotificationDTO notification) throws URISyntaxException {
        log.debug("REST request to save notification : {}", notification);
        Notification userNotification = notifictaionService.createNotification(notification);
        return ResponseEntity.created(new URI("/api/createNotification/" + userNotification.getNotificationId()))
            .headers(HeaderUtil.createAlert( "notification is created with identifier " + userNotification.getNotificationId(), String.valueOf(userNotification.getNotificationId())))
            .body(notification);
    }
}




