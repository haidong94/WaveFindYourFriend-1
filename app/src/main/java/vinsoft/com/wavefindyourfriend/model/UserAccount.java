package vinsoft.com.wavefindyourfriend.model;

/**
 * Created by kienmit95 on 4/10/17.
 */

public class UserAccount {
    private String userName;
    private String userPass;
    private String userPhone;
    private String userAvatar;
    private String createDate;
    private int isOnline;
    private int userSex;

    public UserAccount() {
    }

    public UserAccount(String userName, String userPass, String userPhone, String userAvatar, String createDate, int isOnline, int userSex) {
        this.userName = userName;
        this.userPass = userPass;
        this.userPhone = userPhone;
        this.userAvatar = userAvatar;
        this.createDate = createDate;
        this.isOnline = isOnline;
        this.userSex = userSex;
    }

    public String getUserAvatar() {
        return userAvatar;
    }

    public void setUserAvatar(String userAvatar) {
        this.userAvatar = userAvatar;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public int getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(int isOnline) {
        this.isOnline = isOnline;
    }

    public int getUserSex() {
        return userSex;
    }

    public void setUserSex(int userSex) {
        this.userSex = userSex;
    }
}
