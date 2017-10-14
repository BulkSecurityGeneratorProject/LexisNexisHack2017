package lexisnexis.hackathon.application.web.rest.pushNotification;

import lexisnexis.hackathon.application.domain.Notification;
import lexisnexis.hackathon.application.domain.NotificationModel;
import lexisnexis.hackathon.application.service.PushNotificationService;
import lexisnexis.hackathon.application.service.dto.NotificationDTO;
import lexisnexis.hackathon.application.web.rest.util.HeaderUtil;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author YELESWSX
 * @created 10/13/2017
 */
@RestController
@RequestMapping("/api/notifications")
public class PushNotificationController {
    private final Logger log = LoggerFactory.getLogger(lexisnexis.hackathon.application.web.rest.UserResource.class);

    private final String TOPIC = "d4q90umZRjY:APA91bEP_o9MjmF62EO0efyIZNhbhin_is5RXHLxwoFJEZo1Wkcp-_JYZR4u0FtFLC2qj0iaR2FDR_vP_u0U8NYn-y9jAEXl57jl4rXMrylyT2AAJjERR5MgqXaT_voWt2nE3EJU2bQw";
    private final String COUNSELLINK_WEBSITE = "https://q3cl.examen.com";
    private static final String DEFAULT_FCM_TITLE = "LexisNexisHack2017 server notification";

    @Autowired
    PushNotificationService pushNotificationService;

    /*@PostMapping("/")
    public ResponseEntity createNotification(@Valid @RequestBody NotificationDTO notificationDTO) throws URISyntaxException {
        log.debug("REST request to save notification : {}", notificationDTO);
        Notification userNotification = pushNotificationService.createNotification(notificationDTO);
        return ResponseEntity.created(new URI("/api/createNotification/" + userNotification.getNotificationId()))
            .headers(HeaderUtil.createAlert( "notification is created with identifier " + userNotification.getNotificationId(), String.valueOf(userNotification.getNotificationId())))
            .body(notificationDTO);
    }*/

    @PostMapping("/")
    public ResponseEntity createNotification(@Valid @RequestBody NotificationModel notificationModel) throws URISyntaxException {
        log.debug("REST request to save notification : {}", notificationModel);
        pushNotificationService.createNotification(notificationModel);
        return ResponseEntity.ok().build();
    }

    @RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> send() throws JSONException {
        NotificationModel notification = new NotificationModel();
        notification.setTitle(DEFAULT_FCM_TITLE);
        notification.setBody("Matter 1234567 has been assigned to your firm.");
        notification.setClickAction(COUNSELLINK_WEBSITE);

        CompletableFuture<String> pushNotification = pushNotificationService.send(notification);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        } catch (ExecutionException e) {
            log.error(e.getMessage());
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
