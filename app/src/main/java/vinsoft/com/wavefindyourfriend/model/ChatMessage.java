package vinsoft.com.wavefindyourfriend.model;

/**
 * Created by kienmit95 on 3/30/17.
 */

public class ChatMessage {
    String messageID;
    String personSendID;
    String contentMessage;
    String dateSend;
    String timeSend;
    String messageVision;

    public ChatMessage() {
    }

    public ChatMessage(String messageID, String personSendID, String contentMessage, String dateSend, String timeSend, String messageVision) {
        this.messageID = messageID;
        this.personSendID = personSendID;
        this.contentMessage = contentMessage;
        this.dateSend = dateSend;
        this.timeSend = timeSend;
        this.messageVision = messageVision;
    }

    public String getMessageVision() {
        return messageVision;
    }

    public void setMessageVision(String messageVision) {
        this.messageVision = messageVision;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getPersonSendID() {
        return personSendID;
    }

    public void setPersonSendID(String personSendID) {
        this.personSendID = personSendID;
    }

    public String getContentMessage() {
        return contentMessage;
    }

    public void setContentMessage(String contentMessage) {
        this.contentMessage = contentMessage;
    }

    public String getDateSend() {
        return dateSend;
    }

    public void setDateSend(String dateSend) {
        this.dateSend = dateSend;
    }

    public String getTimeSend() {
        return timeSend;
    }

    public void setTimeSend(String timeSend) {
        this.timeSend = timeSend;
    }

}
