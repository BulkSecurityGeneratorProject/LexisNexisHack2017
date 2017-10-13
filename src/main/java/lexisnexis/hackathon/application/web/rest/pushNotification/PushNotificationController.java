package lexisnexis.hackathon.application.web.rest.pushNotification;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author YELESWSX
 * @created 10/13/2017
 */
@RestController
@RequestMapping("/notifications")
public class PushNotificationController {
    private final String TOPIC = "d4q90umZRjY:APA91bEP_o9MjmF62EO0efyIZNhbhin_is5RXHLxwoFJEZo1Wkcp-_JYZR4u0FtFLC2qj0iaR2FDR_vP_u0U8NYn-y9jAEXl57jl4rXMrylyT2AAJjERR5MgqXaT_voWt2nE3EJU2bQw";
    private final String COUNSELLINK_WEBSITE = "https://q3cl.examen.com";

    @Autowired
    PushNotificationService pushNotificationService;

    @RequestMapping(value = "/send", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> send() throws JSONException {

        JSONObject body = new JSONObject();

        JSONObject notification = new JSONObject();
        notification.put("title", "LexisNexisHack2017 server notification");
        notification.put("body", "Matter 1234567 has been assigned to your firm.");
        notification.put("click_action", COUNSELLINK_WEBSITE);

        body.put("notification", notification);
        body.put("to", TOPIC);

        HttpEntity<String> request = new HttpEntity<>(body.toString());

        CompletableFuture<String> pushNotification = pushNotificationService.send(request);
        CompletableFuture.allOf(pushNotification).join();

        try {
            String firebaseResponse = pushNotification.get();

            return new ResponseEntity<>(firebaseResponse, HttpStatus.OK);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>("Push Notification ERROR!", HttpStatus.BAD_REQUEST);
    }
}
