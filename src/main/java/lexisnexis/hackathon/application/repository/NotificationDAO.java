package lexisnexis.hackathon.application.repository;

import lexisnexis.hackathon.application.domain.NotificationModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.sql.DataSource;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author YELESWSX
 * @created 10/13/2017
 */
@Repository
public class NotificationDAO {
    private final Logger log = LoggerFactory.getLogger(lexisnexis.hackathon.application.web.rest.UserResource.class);

    @Inject
    @Qualifier("dataSourceNonHibernate")
    private DataSource dataSourceNonHibernate;

    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate = new JdbcTemplate(dataSourceNonHibernate);
    }

    public List<NotificationModel> getNotifications() {

        return jdbcTemplate.query(
            "SELECT * " +
                "FROM dbo.notification " +
                "WHERE is_sent=0 " +
                "AND send_time > DATEADD(HOUR, -3, CURRENT_TIMESTAMP)",
            notificationMapper
        );
    }

    RowMapper<NotificationModel> notificationMapper = (rs, rowNum) -> {
        NotificationModel notification = new NotificationModel();
        notification.setNotificationId(rs.getLong("notification_id"));
        notification.setTitle(rs.getString("title"));
        notification.setBody(rs.getString("body"));
        notification.setClickAction(rs.getString("click_action"));
        notification.setData(rs.getBytes("data"));
        notification.setIcon(rs.getString("icon"));
        notification.setLink(rs.getString("link"));
        notification.setRequireInteraction(rs.getBoolean("require_interaction"));
        notification.setTag(rs.getString("tag"));
        notification.setTimeout(rs.getInt("timeout"));
        notification.setVibrate(rs.getString("vibrate"));
        notification.setSilent(rs.getBoolean("silent"));
        notification.setSendTime(rs.getTimestamp("send_time"));
        notification.setSent(rs.getBoolean("is_sent"));
        notification.setSendTo(rs.getString("send_to"));
        return notification;
    };

    public void updateSentNotification(Long notificationId){
        jdbcTemplate.update(
            "UPDATE dbo.notification " +
                "SET is_sent=1 " +
                "WHERE notification_id = " + notificationId.toString()
        );
    }

    public void createNotification(NotificationModel notificationModel){

        /*
        MapSqlParameterSource parameters = new MapSqlParameterSource("title", notificationModel.getTitle());
        parameters
            //.addValue("title", notificationModel.getTitle())
            .addValue("body", notificationModel.getBody())
            .addValue("click_action", notificationModel.getClickAction())
            .addValue("icon", notificationModel.getIcon())
            .addValue("link", notificationModel.getLink())
            .addValue("send_time", notificationModel.getSendTime())
            .addValue("send_to", notificationModel.getSendTo());
        */

        String sendTimeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(notificationModel.getSendTime());

        String query = "notification_insert '" + notificationModel.getTitle() + "', " +
            "'" + notificationModel.getBody() + "', " +
            "'" + notificationModel.getClickAction() + "', " +
            "'" + notificationModel.getIcon() + "', " +
            "'" + notificationModel.getLink() + "', " +
            "'" + sendTimeString + "', " +
            "'" + notificationModel.getSendTo() + "'";

        log.info("query: " + query);

        jdbcTemplate.update(query);
    }
}
