package heinhtetoo.yuelibrary.data.vos;

/**
 * Created by Hein Htet Oo on 11/29/2017.
 */

public class UserVO {

    private String accountId;

    private String displayName;

    private String email;

    private String photoUrl;

    private String phone;

    private String gender;

    private String dateOfBirth;

    private String coverPhoto;

    public UserVO() {
    }

    public UserVO(String accountId, String displayName, String email, String photoUrl, String phone, String gender, String dateOfBirth, String coverPhoto) {
        this.accountId = accountId;
        this.displayName = displayName;
        this.email = email;
        this.photoUrl = photoUrl;
        this.phone = phone;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.coverPhoto = coverPhoto;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getDisplayName() {
        if (displayName != null) {
            return displayName;
        } else {
            return "unavailable";
        }
    }

    public String getEmail() {
        if (email != null) {
            return email;
        } else {
            return "unavailable";
        }
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getPhone() {
        if (phone != null) {
            return phone;
        } else {
            return "unavailable";
        }
    }

    public String getGender() {
        if (gender != null) {
            return gender;
        } else {
            return "unavailable";
        }
    }

    public String getDateOfBirth() {
        if (dateOfBirth != null) {
            return dateOfBirth;
        } else {
            return "unavailable";
        }
    }

    public String getCoverPhoto() {
        return coverPhoto;
    }
}
