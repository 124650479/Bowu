package fu.prm391.sampl.project.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateUserInfoRequest {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("gender")
    @Expose
    private int gender;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
