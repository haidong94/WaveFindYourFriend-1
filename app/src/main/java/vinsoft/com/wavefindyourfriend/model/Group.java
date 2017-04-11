package vinsoft.com.wavefindyourfriend.model;

import java.io.Serializable;

/**
 * Created by kienmit95 on 05/04/17.
 */

public class Group implements Serializable{
    private String groupId;
    private String groupName;
    private String groupCreateDate;
    private String lastMessage;

    public Group() {
    }

    public Group(String groupId, String groupName, String groupCreateDate, String lastMessage) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupCreateDate = groupCreateDate;
        this.lastMessage = lastMessage;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupCreateDate() {
        return groupCreateDate;
    }

    public void setGroupCreateDate(String groupCreateDate) {
        this.groupCreateDate = groupCreateDate;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
