package vinsoft.com.wavefindyourfriend.model;

/**
 * Created by kienmit95 on 4/10/17.
 */

public class Friend {
    private UserAccount userAccount;
    private String createFriendDate;

    public Friend() {
    }

    public Friend(UserAccount userAccount, String createFriendDate) {
        this.userAccount = userAccount;
        this.createFriendDate = createFriendDate;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public String getCreateFriendDate() {
        return createFriendDate;
    }

    public void setCreateFriendDate(String createFriendDate) {
        this.createFriendDate = createFriendDate;
    }
}
