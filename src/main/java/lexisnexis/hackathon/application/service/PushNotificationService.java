package lexisnexis.hackathon.application.service;

import lexisnexis.hackathon.application.domain.Notification;
import lexisnexis.hackathon.application.domain.NotificationModel;
import lexisnexis.hackathon.application.repository.NotificationDAO;
import lexisnexis.hackathon.application.repository.UserNotificationRepository;
import lexisnexis.hackathon.application.service.dto.NotificationDTO;
import lexisnexis.hackathon.application.web.rest.pushNotification.HeaderRequestInterceptor;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author YELESWSX
 * @created 10/13/2017
 */
@Service
@EnableScheduling
@Transactional
public class PushNotificationService {
    private final Logger log = LoggerFactory.getLogger(lexisnexis.hackathon.application.web.rest.UserResource.class);

    private final String TOPIC = "d4q90umZRjY:APA91bEP_o9MjmF62EO0efyIZNhbhin_is5RXHLxwoFJEZo1Wkcp-_JYZR4u0FtFLC2qj0iaR2FDR_vP_u0U8NYn-y9jAEXl57jl4rXMrylyT2AAJjERR5MgqXaT_voWt2nE3EJU2bQw";
    private static final String FIREBASE_SERVER_KEY = "key=AAAAIMWyHGY:APA91bEm9z_ldZWH3Ocu1CbfmCsWuw7A6u9nw-XiaViKt20etXMYCDtsIbYn0YvAhpY6MXnzXveyQbSNa6BotGTxm3UYQogmvNXMq9dWYsk-2WbxwmAfcuBHaaehzCImFGZVstCwBW2Z";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Scheduled(fixedRate = 10000, initialDelay = 5000)
    public void fixedRatePullNotificationData() throws JSONException {
        // call rest-api provided by Ravi to pull the data from our local database
        // Notification notification = userNotificationRepository.findOneByNotificationId();

        List<NotificationModel> pendingNotifications = notificationDAO.getNotifications();

        pendingNotifications.forEach(notification -> {
            CompletableFuture<String> pushNotification = send(notification);
            CompletableFuture.allOf(pushNotification).join();

            notificationDAO.updateSentNotification(notification.getNotificationId());
            try {
                String firebaseResponse = pushNotification.get();
                log.info("firebaseResponse = " + firebaseResponse);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            } catch (ExecutionException e) {
                log.error(e.getMessage());
            }
        });
    }

    @Async
    public CompletableFuture<String> send(NotificationModel notification){

        try {
            JSONObject body = new JSONObject();
            JSONObject fcmNotification = new JSONObject();
            fcmNotification.put("title", notification.getTitle());
            fcmNotification.put("body", notification.getBody());
            fcmNotification.put("click_action", notification.getClickAction());
            fcmNotification.put("icon", notification.getIcon());

            body.put("notification", fcmNotification);
            body.put("to", notification.getSendTo());

            HttpEntity<String> request = new HttpEntity<>(body.toString());

            RestTemplate restTemplate = new RestTemplate();

            ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add(new HeaderRequestInterceptor("Authorization", FIREBASE_SERVER_KEY));
            interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
            restTemplate.setInterceptors(interceptors);

            String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, request, String.class);

            return CompletableFuture.completedFuture(firebaseResponse);
        } catch(JSONException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /*public Notification createNotification(NotificationDTO notificationDTO){
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
    }*/

    public void createNotification(NotificationModel notificationModel) {
        notificationDAO.createNotification(notificationModel);
    }
}
