package lexisnexis.hackathon.application.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "Notification")
public class Notification {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private long notificationId;

    @Size(max = 1024)
    @Column(name = "body", length = 1024)
    private String body;

    @Size(max = 1024)
    @Column(name = "data", length = 1024)
    private byte[] data;

    @Size(max = 256)
    @Column(name = "icon", length = 256)
    private String icon;

    @Size(max = 256)
    @Column(name = "link", length = 256)
    private String link;

    @Column(name = "require_interaction")
    private Boolean requireInteraction;

    @Size(max = 256)
    @Column(name = "tag", length = 256)
    private String tag;

    @Column(name = "timeout")
    private int timeout;

    @Size(max = 256)
    @Column(name = "vibrate", length = 256)
    private String vibrate;

    @Column(name = "silent")
    private Boolean silent;

    @Column(name = "send_time")
    private java.util.Date sendTime;

    @Column(name = "is_sent")
    private Boolean isSent;

    @Size(max = 256)
    @Column(name = "sendTo", length = 256)
    private String sendTo;

    public long getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(long notificationId) {
        this.notificationId = notificationId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getRequireInteraction() {
        return requireInteraction;
    }

    public void setRequireInteraction(Boolean requireInteraction) {
        this.requireInteraction = requireInteraction;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getVibrate() {
        return vibrate;
    }

    public void setVibrate(String vibrate) {
        this.vibrate = vibrate;
    }

    public Boolean getSilent() {
        return silent;
    }

    public void setSilent(Boolean silent) {
        this.silent = silent;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Boolean getSent() {
        return isSent;
    }

    public void setSent(Boolean sent) {
        isSent = sent;
    }

    public String getSendTo() {
        return sendTo;
    }

    public void setSendTo(String sendTo) {
        this.sendTo = sendTo;
    }
}
