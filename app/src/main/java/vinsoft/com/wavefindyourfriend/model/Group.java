package vinsoft.com.wavefindyourfriend.model;

/**
 * Created by kienmit95 on 05/04/17.
 */

public class Group {
    private String groupId;
    private String groupName;
    private String groupCreateDate;

    public Group() {
    }

    public Group(String groupId, String groupName, String groupCreateDate) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.groupCreateDate = groupCreateDate;
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
}
