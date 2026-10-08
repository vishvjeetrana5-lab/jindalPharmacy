package dataman.dmToolkit.dmNotifications.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DmNotification_Mdl implements Serializable, IsSerializable {

    private String id;
    private String messageTitle, message, messageDate, messageTime;
    private String fromUser;
    private List<String> toUserList = new ArrayList<>();
    private DmNotificationType notificationType = DmNotificationType.NONE;
    private String moduleName, menuName, keyValue;
    private boolean status;
    private TransportationStatus statusTransportation = TransportationStatus.SENDING_TO_SERVER;
    private String statusMessage;

    public DmNotification_Mdl() {
    }
    private boolean broadCast;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageTitle() {
        return messageTitle;
    }

    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public List<String> getToUserList() {
        return toUserList;
    }

    public void setToUserList(List<String> toUserList) {
        this.toUserList = toUserList;
    }

    public DmNotificationType getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(DmNotificationType notificationType) {
        this.notificationType = notificationType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public TransportationStatus getStatusTransportation() {
        return statusTransportation;
    }

    public void setStatusTransportation(TransportationStatus statusTransportation) {
        this.statusTransportation = statusTransportation;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    public boolean isBroadCast() {
        return broadCast;
    }

    public void setBroadCast(boolean broadCast) {
        this.broadCast = broadCast;
    }

}
