package lexisnexis.hackathon.application.web.rest.pushNotification;

import org.springframework.http.HttpEntity;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

/**
 * @author YELESWSX
 * @created 10/13/2017
 */
@Service
@EnableScheduling
public class PushNotificationService {
    private static final String FIREBASE_SERVER_KEY = "key=AAAAIMWyHGY:APA91bEm9z_ldZWH3Ocu1CbfmCsWuw7A6u9nw-XiaViKt20etXMYCDtsIbYn0YvAhpY6MXnzXveyQbSNa6BotGTxm3UYQogmvNXMq9dWYsk-2WbxwmAfcuBHaaehzCImFGZVstCwBW2Z";
    private static final String FIREBASE_API_URL = "https://fcm.googleapis.com/fcm/send";

    @Scheduled(fixedRate = 5000, initialDelay = 5000)
    public void fixedRatePullNotificationData() {
        // call rest-api provided by Ravi to pull the data from our local database
    }

    @Async
    public CompletableFuture<String> send(HttpEntity<String> entity) {

        RestTemplate restTemplate = new RestTemplate();

        ArrayList<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "key=" + FIREBASE_SERVER_KEY));
        interceptors.add(new HeaderRequestInterceptor("Content-Type", "application/json"));
        restTemplate.setInterceptors(interceptors);

        String firebaseResponse = restTemplate.postForObject(FIREBASE_API_URL, entity, String.class);

        return CompletableFuture.completedFuture(firebaseResponse);
    }
}
